window.App = window.App || {};
App.render = App.render || {};
App.actions = App.actions || {};

App.render.carrito = function(){
  const { state, utils } = App;
  const lineas = state.carrito.map(l => ({...l, producto: utils.findProducto(l.productoId)})).filter(l => l.producto);
  const app = document.getElementById('app');

  if(lineas.length === 0){
    app.innerHTML = `
      <div class="panel-head"><div><div class="path">Menú principal</div><h2>Carrito de compras</h2></div></div>
      <div class="empty"><span class="big">🕸️</span>Tu carrito está vacío. Agregá productos desde "Gestionar productos".</div>`;
    return;
  }

  app.innerHTML = `
    <div class="panel-head"><div><div class="path">Menú principal</div><h2>Carrito de compras</h2></div></div>
    <table>
      <thead><tr><th>Producto</th><th>Precio unit.</th><th>Cantidad</th><th>Subtotal</th><th></th></tr></thead>
      <tbody>
        ${lineas.map(l => `
          <tr>
            <td>${utils.esc(l.producto.nombre)}<div class="id-tag">ID #${l.producto.id}</div></td>
            <td class="num-cell">${utils.fmtMoney(l.producto.precio)}</td>
            <td><input class="qty-input" type="number" min="1" max="${l.producto.stock}" value="${l.cantidad}" data-action="cart-qty" data-id="${l.producto.id}"></td>
            <td class="num-cell">${utils.fmtMoney(l.producto.precio * l.cantidad)}</td>
            <td><button class="btn ghost small" data-action="cart-remove" data-id="${l.producto.id}">Quitar</button></td>
          </tr>`).join('')}
      </tbody>
    </table>
    <div class="total-line"><span>Total</span><span>${utils.fmtMoney(utils.cartTotal())}</span></div>
    <div class="form-actions" style="margin-top:16px;">
      <button class="btn" data-action="nav" data-vista="pedido">Realizar pedido →</button>
      <button class="btn ghost" data-action="cart-clear">Vaciar carrito</button>
    </div>`;
};

App.actions.addToCart = function(productoId, inputEl){
  const { state, utils } = App;
  const p = utils.findProducto(productoId);
  if(!p) return;
  let cantidad = parseInt(inputEl?.value, 10);
  if(!Number.isInteger(cantidad) || cantidad < 1) cantidad = 1;
  if(cantidad > p.stock){
    utils.toast(`Solo hay ${p.stock} unidades disponibles de "${p.nombre}".`, 'error');
    return;
  }
  const existente = state.carrito.find(l => l.productoId === productoId);
  const yaEnCarrito = existente ? existente.cantidad : 0;
  if(yaEnCarrito + cantidad > p.stock){
    utils.toast(`No podés agregar más: ya tenés ${yaEnCarrito} en el carrito y solo hay ${p.stock} en stock.`, 'error');
    return;
  }
  if(existente) existente.cantidad += cantidad;
  else state.carrito.push({productoId, cantidad});
  utils.toast(`"${p.nombre}" agregado al carrito.`);
  App.render.all();
};

App.actions.updateCartQty = function(productoId, cantidad){
  const { state, utils } = App;
  const p = utils.findProducto(productoId);
  const linea = state.carrito.find(l => l.productoId === productoId);
  if(!p || !linea) return;
  cantidad = parseInt(cantidad, 10);
  if(!Number.isInteger(cantidad) || cantidad < 1){
    utils.toast('La cantidad debe ser un número mayor a 0.', 'error');
    App.render.all();
    return;
  }
  if(cantidad > p.stock){
    utils.toast(`Solo hay ${p.stock} unidades disponibles.`, 'error');
    cantidad = p.stock;
  }
  linea.cantidad = cantidad;
  App.render.all();
};

App.actions.removeFromCart = function(productoId){
  App.state.carrito = App.state.carrito.filter(l => l.productoId !== productoId);
  App.render.all();
};

App.actions.clearCart = function(){
  App.state.carrito = [];
  App.render.all();
};
