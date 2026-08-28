window.App = window.App || {};
App.render = App.render || {};
App.actions = App.actions || {};

App.render.pedido = function(){
  const { state, utils } = App;
  const lineas = state.carrito.map(l => ({...l, producto: utils.findProducto(l.productoId)})).filter(l => l.producto);
  const app = document.getElementById('app');

  if(lineas.length === 0){
    app.innerHTML = `
      <div class="panel-head"><div><div class="path">Menú principal</div><h2>Realizar pedido</h2></div></div>
      <div class="empty"><span class="big">📝</span>No hay productos en el carrito para armar un pedido.</div>`;
    return;
  }

  app.innerHTML = `
    <div class="panel-head"><div><div class="path">Menú principal</div><h2>Realizar pedido</h2></div></div>
    <div class="ticket" style="max-width:640px;">
      <div class="id-tag">Resumen del pedido · usuario ${utils.esc(state.usuario)}</div>
      <table style="margin-top:8px;">
        <thead><tr><th>Producto</th><th>Cant.</th><th>Subtotal</th></tr></thead>
        <tbody>
          ${lineas.map(l => `
            <tr>
              <td>${utils.esc(l.producto.nombre)}</td>
              <td class="num-cell">${l.cantidad}</td>
              <td class="num-cell">${utils.fmtMoney(l.producto.precio * l.cantidad)}</td>
            </tr>`).join('')}
        </tbody>
      </table>
      <div class="total-line"><span>Total a pagar</span><span>${utils.fmtMoney(utils.cartTotal())}</span></div>
    </div>
    <div class="form-actions" style="margin-top:16px;">
      <button class="btn" data-action="confirmar-pedido">Confirmar y crear pedido</button>
      <button class="btn ghost" data-action="nav" data-vista="carrito">← Volver al carrito</button>
    </div>`;
};

App.render.historial = function(){
  const { state, utils } = App;
  const pedidos = state.pedidos.filter(p => p.usuario === state.usuario).slice().reverse();
  const app = document.getElementById('app');

  if(pedidos.length === 0){
    app.innerHTML = `
      <div class="panel-head"><div><div class="path">Menú principal</div><h2>Historial de pedidos</h2></div></div>
      <div class="empty"><span class="big">📜</span>Todavía no realizaste ningún pedido.</div>`;
    return;
  }

  app.innerHTML = `
    <div class="panel-head"><div><div class="path">Menú principal</div><h2>Historial de pedidos · ${utils.esc(state.usuario)}</h2></div></div>
    ${pedidos.map(p => `
      <div class="ticket" style="margin-bottom:14px;">
        <div style="display:flex; justify-content:space-between; align-items:flex-start; flex-wrap:wrap; gap:8px;">
          <div><div class="id-tag">Pedido #${p.id} · ${utils.fmtFecha(p.fecha)}</div></div>
          ${utils.estadoTag(p.estado)}
        </div>
        <div class="ticket-tear"></div>
        <table>
          <tbody>
            ${p.lineas.map(l => `
              <tr><td>${utils.esc(l.nombre)} × ${l.cantidad}</td><td class="num-cell">${utils.fmtMoney(l.subtotal)}</td></tr>`).join('')}
          </tbody>
        </table>
        <div class="total-line" style="font-size:15px;"><span>Total</span><span>${utils.fmtMoney(p.total)}</span></div>
        <div class="form-actions" style="margin-top:12px;">${pedidoAcciones(p)}</div>
      </div>`).join('')}`;
};

function pedidoAcciones(p){
  if(p.estado === 'pendiente'){
    return `
      <button class="btn small" data-action="pedido-confirmar" data-id="${p.id}">Confirmar pedido</button>
      <button class="btn ghost small" data-action="pedido-cancelar" data-id="${p.id}">Cancelar pedido</button>`;
  }
  if(p.estado === 'confirmado'){
    return `
      <button class="btn small" data-action="pedido-enviar" data-id="${p.id}">Marcar como enviado</button>
      <button class="btn ghost small" data-action="pedido-cancelar" data-id="${p.id}">Cancelar pedido</button>`;
  }
  if(p.estado === 'enviado'){
    return `<button class="btn small" data-action="pedido-entregar" data-id="${p.id}">Marcar como entregado</button>`;
  }
  return `<span style="font-size:11px; color:var(--ink-faint);">Pedido finalizado</span>`;
}

App.actions.confirmarPedidoDesdeCarrito = function(){
  const { state, models, utils } = App;
  const lineasFuente = state.carrito.map(l => ({...l, producto: utils.findProducto(l.productoId)})).filter(l => l.producto);

  const insuficientes = lineasFuente.filter(l => l.cantidad > l.producto.stock);
  if(insuficientes.length > 0){
    utils.toast(`Stock insuficiente para: ${insuficientes.map(l=>l.producto.nombre).join(', ')}.`, 'error');
    App.render.all();
    return;
  }

  const lineas = lineasFuente.map(l => new models.LineaPedido(l.producto, l.cantidad));
  const pedido = new models.Pedido(state.nextPedidoId++, state.usuario, lineas);
  state.pedidos.push(pedido);
  state.carrito = [];
  utils.toast(`Pedido #${pedido.id} creado como "pendiente".`);
  state.vista = 'historial';
  App.render.all();
};

App.actions.pedidoConfirmar = function(id){
  const { utils } = App;
  const pedido = utils.findPedido(id);
  if(!pedido || pedido.estado !== 'pendiente') return;
  const faltantes = pedido.lineas.filter(l => {
    const p = utils.findProducto(l.productoId);
    return !p || p.stock < l.cantidad;
  });
  if(faltantes.length > 0){
    utils.toast(`No se puede confirmar: stock insuficiente de ${faltantes.map(l=>l.nombre).join(', ')}.`, 'error');
    return;
  }
  pedido.lineas.forEach(l => { utils.findProducto(l.productoId).stock -= l.cantidad; });
  pedido.estado = 'confirmado';
  utils.toast(`Pedido #${pedido.id} confirmado. Stock actualizado.`);
  App.render.all();
};

App.actions.pedidoCancelar = function(id){
  const { utils } = App;
  const pedido = utils.findPedido(id);
  if(!pedido) return;
  if(pedido.estado === 'confirmado' || pedido.estado === 'enviado'){
    pedido.lineas.forEach(l => {
      const p = utils.findProducto(l.productoId);
      if(p) p.stock += l.cantidad;
    });
  }
  pedido.estado = 'cancelado';
  utils.toast(`Pedido #${pedido.id} cancelado.`);
  App.render.all();
};

App.actions.pedidoEnviar = function(id){
  const { utils } = App;
  const pedido = utils.findPedido(id);
  if(!pedido || pedido.estado !== 'confirmado') return;
  pedido.estado = 'enviado';
  utils.toast(`Pedido #${pedido.id} marcado como enviado.`);
  App.render.all();
};

App.actions.pedidoEntregar = function(id){
  const { utils } = App;
  const pedido = utils.findPedido(id);
  if(!pedido || pedido.estado !== 'enviado') return;
  pedido.estado = 'entregado';
  utils.toast(`Pedido #${pedido.id} entregado.`);
  App.render.all();
};
