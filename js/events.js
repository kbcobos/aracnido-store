window.App = window.App || {};

document.addEventListener('click', (e) => {
  const el = e.target.closest('[data-action]');
  if(!el) return;
  const { state, actions } = App;
  const action = el.dataset.action;

  if(action === 'nav'){
    state.vista = el.dataset.vista;
    if(state.vista === 'productos') state.subProductos = 'listar';
    state.seleccionId = null;
    App.render.all();
  }
  else if(action === 'sub-productos'){
    state.subProductos = el.dataset.sub;
    state.seleccionId = null;
    App.render.all();
  }
  else if(action === 'add-to-cart'){
    const input = document.getElementById(`qty-${el.dataset.id}`);
    actions.addToCart(Number(el.dataset.id), input);
  }
  else if(action === 'cart-remove'){
    actions.removeFromCart(Number(el.dataset.id));
  }
  else if(action === 'cart-clear'){
    actions.clearCart();
  }
  else if(action === 'confirmar-pedido'){
    actions.confirmarPedidoDesdeCarrito();
  }
  else if(action === 'pedido-confirmar'){
    actions.pedidoConfirmar(Number(el.dataset.id));
  }
  else if(action === 'pedido-cancelar'){
    actions.pedidoCancelar(Number(el.dataset.id));
  }
  else if(action === 'pedido-enviar'){
    actions.pedidoEnviar(Number(el.dataset.id));
  }
  else if(action === 'pedido-entregar'){
    actions.pedidoEntregar(Number(el.dataset.id));
  }
  else if(action === 'eliminar-producto-confirm'){
    actions.eliminarProductoConfirm(Number(el.dataset.id));
  }
  else if(action === 'eliminar-producto-cancel'){
    state.seleccionId = null;
    App.render.all();
  }
  else if(action === 'cat-delete'){
    actions.eliminarCategoria(el.dataset.cat);
  }
  else if(action === 'theme-toggle'){
    App.theme.toggle();
  }
});

document.addEventListener('change', (e) => {
  const el = e.target;
  if(el.dataset.action === 'select-producto'){
    App.state.seleccionId = el.value ? Number(el.value) : null;
    App.render.all();
  }
  if(el.dataset.action === 'cart-qty'){
    App.actions.updateCartQty(Number(el.dataset.id), el.value);
  }
});

document.addEventListener('submit', (e) => {
  const form = e.target.closest('form[data-form]');
  if(!form) return;
  e.preventDefault();
  const type = form.dataset.form;
  const data = Object.fromEntries(new FormData(form).entries());

  if(type === 'agregar-producto')    App.actions.agregarProducto(data);
  if(type === 'buscar-producto')     App.actions.buscarProducto(data.q);
  if(type === 'actualizar-producto') App.actions.actualizarProducto(data);
  if(type === 'agregar-categoria')   App.actions.agregarCategoria(data.nombre);
});
