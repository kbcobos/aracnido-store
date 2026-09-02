window.App = window.App || {};
App.render = App.render || {};

App.render.admin = function(){
  const { state, utils } = App;
  const bajos = state.productos.filter(p => p.stock <= App.STOCK_MINIMO).sort((a,b)=>a.stock-b.stock);
  const pendientes = state.pedidos.filter(p => p.estado === 'pendiente').length;

  document.getElementById('app').innerHTML = `
    <div class="panel-head"><div><div class="path">Menú principal</div><h2>Administración — usuarios y stock</h2></div></div>

    <div class="kpi-row">
      <div class="kpi"><div class="n">${state.productos.length}</div><div class="l">Productos en catálogo</div></div>
      <div class="kpi"><div class="n">${state.pedidos.length}</div><div class="l">Pedidos totales</div></div>
      <div class="kpi"><div class="n">${pendientes}</div><div class="l">Pedidos pendientes</div></div>
      <div class="kpi"><div class="n">${bajos.length}</div><div class="l">Alertas de stock</div></div>
    </div>

    <div class="divider-label">Alertas de stock (mínimo: ${App.STOCK_MINIMO} unidades)</div>
    ${bajos.length === 0 ? `<div class="empty">Sin alertas. Todo el stock está por encima del mínimo.</div>` :
      bajos.map(p => `
        <div class="ticket" style="display:flex; justify-content:space-between; align-items:center; margin-bottom:10px;">
          <div>
            <div class="id-tag">ID #${p.id} · ${utils.esc(p.categoria)}</div>
            <strong>${utils.esc(p.nombre)}</strong>
          </div>
          ${p.stock === 0 ? `<span class="web-tag agotado">sin stock</span>` : `<span class="web-tag bajo">quedan ${p.stock}</span>`}
        </div>`).join('')}

    <div class="divider-label">Todos los pedidos</div>
    ${state.pedidos.length === 0 ? `<div class="empty">Todavía no se registraron pedidos en el sistema.</div>` : `
    <table>
      <thead><tr><th>ID</th><th>Usuario</th><th>Fecha</th><th>Estado</th><th>Total</th></tr></thead>
      <tbody>
        ${state.pedidos.slice().reverse().map(p => `
          <tr>
            <td>#${p.id}</td>
            <td>${utils.esc(p.usuario)}</td>
            <td>${utils.fmtFecha(p.fecha)}</td>
            <td>${utils.estadoTag(p.estado)}</td>
            <td class="num-cell">${utils.fmtMoney(p.total)}</td>
          </tr>`).join('')}
      </tbody>
    </table>`}`;
};
