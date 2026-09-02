window.App = window.App || {};
App.render = App.render || {};

App.MENU = [
  {n:1, id:'productos',   label:'Gestionar productos'},
  {n:2, id:'categorias',  label:'Gestionar categorías'},
  {n:3, id:'carrito',     label:'Ver carrito de compras'},
  {n:4, id:'pedido',      label:'Realizar pedido'},
  {n:5, id:'historial',   label:'Consultar historial de pedidos'},
  {n:6, id:'admin',       label:'Administración (usuarios y stock)'},
  {n:7, id:'salir',       label:'Salir'},
];

App.render.menu = function(){
  const { state } = App;
  const nav = document.getElementById('main-menu');
  nav.innerHTML = `
    <div class="menu-title">Menú principal</div>
    <ul>
      ${App.MENU.map(m => `
        <li>
          <button data-action="nav" data-vista="${m.id}" class="${state.vista===m.id ? 'active':''}">
            <span class="num">${m.n})</span> ${m.label}
          </button>
        </li>`).join('')}
    </ul>`;
  document.getElementById('usuario-label').textContent = state.usuario;
  document.getElementById('cart-badge').textContent = App.utils.cartCount();
};

App.render.inicio = function(){
  document.getElementById('app').innerHTML = `
    <div class="ticket" style="max-width:640px;">
      <div class="id-tag">Bienvenida</div>
      <h2 style="margin:6px 0 10px;">Elegí una opción del menú principal</h2>
      <div style="color:var(--ink-soft);">
        Desde acá podés gestionar el catálogo arácnido, armar y confirmar pedidos,
        y revisar el estado del stock. El menú de la izquierda replica las mismas
        opciones numeradas que tendría la versión de consola del sistema.
      </div>
    </div>`;
};

App.render.salir = function(){
  document.getElementById('app').innerHTML = `
    <div class="ticket" style="max-width:480px; margin:30px auto; text-align:center;">
      <h2 style="margin-bottom:8px;">¡Gracias por visitar Arácnido Store!</h2>
      <div style="color:var(--ink-soft); margin-bottom:16px;">Tus productos y pedidos quedaron guardados en esta sesión.</div>
      <button class="btn" data-action="nav" data-vista="inicio">Volver al menú principal</button>
    </div>`;
};

App.render.all = function(){
  App.render.menu();
  const map = {
    inicio: App.render.inicio,
    productos: App.render.productos,
    categorias: App.render.categorias,
    carrito: App.render.carrito,
    pedido: App.render.pedido,
    historial: App.render.historial,
    admin: App.render.admin,
    salir: App.render.salir,
  };
  (map[App.state.vista] || App.render.inicio)();
};
