window.App = window.App || {};
App.render = App.render || {};
App.actions = App.actions || {};

const SUB_PRODUCTOS = [
  {k:'a', id:'agregar',    label:'Agregar producto'},
  {k:'b', id:'listar',     label:'Listar productos'},
  {k:'c', id:'buscar',     label:'Buscar producto por ID / nombre'},
  {k:'d', id:'actualizar', label:'Actualizar producto'},
  {k:'e', id:'eliminar',   label:'Eliminar producto'},
  {k:'f', id:'volver',     label:'Volver al menú principal'},
];
App.SUB_PRODUCTOS = SUB_PRODUCTOS;

App.render.productos = function(){
  const { state, utils } = App;

  if(state.subProductos === 'volver'){
    state.vista = 'inicio';
    state.subProductos = 'listar';
    return App.render.all();
  }

  const subnav = `
    <div class="subnav">
      ${SUB_PRODUCTOS.map(s => `
        <button data-action="sub-productos" data-sub="${s.id}" class="${state.subProductos===s.id?'active':''}">
          <span class="k">${s.k})</span>${s.label}
        </button>`).join('')}
    </div>`;

  let body = '';
  if(state.subProductos === 'listar')     body = renderListar();
  if(state.subProductos === 'agregar')    body = renderAgregar();
  if(state.subProductos === 'buscar')     body = renderBuscar();
  if(state.subProductos === 'actualizar') body = renderActualizar();
  if(state.subProductos === 'eliminar')   body = renderEliminar();

  document.getElementById('app').innerHTML = `
    <div class="panel-head">
      <div>
        <div class="path">Gestión de productos</div>
        <h2>${SUB_PRODUCTOS.find(s=>s.id===state.subProductos)?.label ?? ''}</h2>
      </div>
    </div>
    ${subnav}
    ${body}`;
};

function renderListar(){
  const { state, utils } = App;
  if(state.productos.length === 0){
    return `<div class="empty"><span class="big">🕸️</span>No hay productos cargados todavía.</div>`;
  }
  return `<div class="grid">
    ${state.productos.map(p => `
      <div class="ticket prod-card">
        <div class="thumb">${utils.iconoCategoria(p.icono)}</div>
        <div class="body">
          <span class="cat">${utils.esc(p.categoria)}</span>
          <div class="id-tag">ID #${p.id}</div>
          <h3>${utils.esc(p.nombre)}</h3>
          <div class="desc">${utils.esc(p.descripcion)}</div>
          <div class="price-row">
            <span class="price">${utils.fmtMoney(p.precio)}</span>
            <span class="stock">${utils.stockLabel(p)}</span>
          </div>
          <div class="add-row">
            <input type="number" min="1" max="${Math.max(p.stock,1)}" value="1" id="qty-${p.id}" ${p.stock===0?'disabled':''}>
            <button class="btn small" data-action="add-to-cart" data-id="${p.id}" ${p.stock===0?'disabled':''}>Agregar al carrito</button>
          </div>
        </div>
      </div>`).join('')}
  </div>`;
}

function renderAgregar(){
  const { state, utils } = App;
  return `
    <form class="stack" data-form="agregar-producto">
      <div class="field">
        <label>Nombre</label>
        <input type="text" name="nombre" required maxlength="80" placeholder="Ej: Guantes con efecto telaraña">
      </div>
      <div class="field">
        <label>Descripción</label>
        <textarea name="descripcion" required maxlength="300" placeholder="Breve descripción del producto"></textarea>
      </div>
      <div class="field">
        <label>Precio (ARS)</label>
        <input type="number" name="precio" min="1" step="1" required placeholder="0">
      </div>
      <div class="field">
        <label>Categoría</label>
        <select name="categoria" required>
          ${state.categorias.map(c => `<option value="${utils.esc(c)}">${utils.esc(c)}</option>`).join('')}
        </select>
        <div class="hint">¿Necesitás una categoría nueva? Creala primero en "Gestionar categorías".</div>
      </div>
      <div class="field">
        <label>Ícono de producto</label>
        <select name="icono">
          <option value="figura">Figura</option>
          <option value="mascara">Máscara</option>
          <option value="remera">Indumentaria</option>
          <option value="poster">Poster</option>
          <option value="comic">Cómic</option>
          <option value="mochila">Mochila</option>
          <option value="taza">Taza</option>
          <option value="llavero">Llavero / accesorio</option>
        </select>
      </div>
      <div class="field">
        <label>Stock inicial</label>
        <input type="number" name="stock" min="0" step="1" required placeholder="0">
      </div>
      <div class="form-actions">
        <button type="submit" class="btn">Agregar producto</button>
        <button type="reset" class="btn ghost">Limpiar</button>
      </div>
    </form>`;
}

function renderBuscar(){
  const { state, utils } = App;
  const q = state.busqueda.trim().toLowerCase();
  let resultados = [];
  if(q){
    resultados = state.productos.filter(p =>
      String(p.id) === q || p.nombre.toLowerCase().includes(q)
    );
  }
  return `
    <form class="stack" data-form="buscar-producto" style="flex-direction:row; align-items:flex-end; max-width:600px;">
      <div class="field" style="flex:1; margin:0;">
        <label>Buscar por ID o nombre</label>
        <input type="text" name="q" value="${utils.esc(state.busqueda)}" placeholder="Ej: 3, o &quot;máscara&quot;">
      </div>
      <button type="submit" class="btn">Buscar</button>
    </form>
    <div style="margin-top:18px;">
      ${q === '' ? `<div class="empty">Ingresá un ID o parte del nombre para buscar.</div>` :
        resultados.length === 0 ? `<div class="empty"><span class="big">🔍</span>No se encontró ningún producto para "${utils.esc(state.busqueda)}".</div>` :
        resultados.map(p => renderDetalle(p)).join('')}
    </div>`;
}

function renderDetalle(p){
  const { utils } = App;
  return `
    <div class="ticket" style="max-width:520px;">
      <div class="id-tag">ID #${p.id} · ${utils.esc(p.categoria)}</div>
      <h3 style="margin:4px 0 8px;">${utils.esc(p.nombre)}</h3>
      <div style="color:var(--ink-soft); margin-bottom:10px;">${utils.esc(p.descripcion)}</div>
      <div class="ticket-tear"></div>
      <table style="margin-top:4px;">
        <tr><td>Precio</td><td class="num-cell">${utils.fmtMoney(p.precio)}</td></tr>
        <tr><td>Stock</td><td class="num-cell">${utils.stockLabel(p)}</td></tr>
      </table>
    </div>`;
}

function renderActualizar(){
  const { state, utils } = App;
  const sel = state.seleccionId ? utils.findProducto(state.seleccionId) : null;
  const selector = `
    <div class="field" style="max-width:420px;">
      <label>Seleccioná un producto</label>
      <select data-action="select-producto">
        <option value="">— elegir producto —</option>
        ${state.productos.map(p => `<option value="${p.id}" ${sel && sel.id===p.id ? 'selected':''}>#${p.id} · ${utils.esc(p.nombre)}</option>`).join('')}
      </select>
    </div>`;

  if(!sel){
    return `${selector}<div class="empty" style="margin-top:16px;"><span class="big">✏️</span>Elegí un producto para actualizar su precio o stock.</div>`;
  }

  return `
    ${selector}
    <form class="stack" data-form="actualizar-producto" style="margin-top:18px;">
      <input type="hidden" name="id" value="${sel.id}">
      <div class="ticket" style="margin-bottom:4px;">
        <div class="id-tag">ID #${sel.id} · ${utils.esc(sel.categoria)}</div>
        <h3 style="margin:4px 0;">${utils.esc(sel.nombre)}</h3>
      </div>
      <div class="field">
        <label>Precio (ARS)</label>
        <input type="number" name="precio" min="1" step="1" value="${sel.precio}" required>
      </div>
      <div class="field">
        <label>Stock</label>
        <input type="number" name="stock" min="0" step="1" value="${sel.stock}" required>
        <div class="hint">El stock no puede ser un valor negativo.</div>
      </div>
      <div class="form-actions">
        <button type="submit" class="btn">Guardar cambios</button>
      </div>
    </form>`;
}

function renderEliminar(){
  const { state, utils } = App;
  const sel = state.seleccionId ? utils.findProducto(state.seleccionId) : null;
  const selector = `
    <div class="field" style="max-width:420px;">
      <label>Seleccioná un producto</label>
      <select data-action="select-producto">
        <option value="">— elegir producto —</option>
        ${state.productos.map(p => `<option value="${p.id}" ${sel && sel.id===p.id ? 'selected':''}>#${p.id} · ${utils.esc(p.nombre)}</option>`).join('')}
      </select>
    </div>`;

  if(!sel){
    return `${selector}<div class="empty" style="margin-top:16px;"><span class="big">🗑️</span>Elegí un producto para eliminarlo del catálogo.</div>`;
  }

  return `
    ${selector}
    <div class="ticket" style="margin-top:18px; max-width:480px; border-color:var(--red);">
      <div class="id-tag">ID #${sel.id} · ${utils.esc(sel.categoria)}</div>
      <h3 style="margin:4px 0 8px;">${utils.esc(sel.nombre)}</h3>
      <div style="color:var(--ink-soft); margin-bottom:12px;">
        Esta acción es permanente y quitará el producto del catálogo${state.carrito.some(l=>l.productoId===sel.id) ? ' y de tu carrito' : ''}.
      </div>
      <div class="form-actions">
        <button class="btn danger" data-action="eliminar-producto-confirm" data-id="${sel.id}">Confirmar eliminación</button>
        <button class="btn ghost" data-action="eliminar-producto-cancel">Cancelar</button>
      </div>
    </div>`;
}

App.actions.agregarProducto = function(data){
  const { state, models, utils } = App;
  const nombre = (data.nombre || '').trim();
  const descripcion = (data.descripcion || '').trim();
  const precio = Number(data.precio);
  const stock = Number(data.stock);
  const categoria = data.categoria;
  const icono = data.icono || 'figura';

  if(!nombre || !descripcion || !categoria){
    utils.toast('Completá todos los campos del formulario.', 'error'); return;
  }
  if(!Number.isFinite(precio) || precio <= 0){
    utils.toast('El precio debe ser un número mayor a 0.', 'error'); return;
  }
  if(!Number.isInteger(stock) || stock < 0){
    utils.toast('El stock debe ser un número entero, mayor o igual a 0.', 'error'); return;
  }
  const nuevo = new models.Producto({id: state.nextProductId++, nombre, descripcion, precio, categoria, icono, stock});
  state.productos.push(nuevo);
  utils.toast(`Producto "${nombre}" agregado con ID #${nuevo.id}.`);
  state.subProductos = 'listar';
  App.render.all();
};

App.actions.buscarProducto = function(query){
  App.state.busqueda = query || '';
  App.render.all();
};

App.actions.actualizarProducto = function(data){
  const { utils } = App;
  const p = utils.findProducto(Number(data.id));
  if(!p) return;
  const precio = Number(data.precio);
  const stock = Number(data.stock);
  if(!Number.isFinite(precio) || precio <= 0){
    utils.toast('El precio debe ser un número mayor a 0.', 'error'); return;
  }
  if(!Number.isInteger(stock) || stock < 0){
    utils.toast('El stock no puede ser negativo.', 'error'); return;
  }
  p.precio = precio;
  p.stock = stock;
  utils.toast(`Producto #${p.id} actualizado.`);
  App.render.all();
};

App.actions.eliminarProductoConfirm = function(id){
  const { state, utils } = App;
  state.productos = state.productos.filter(p => p.id !== id);
  state.carrito = state.carrito.filter(l => l.productoId !== id);
  state.seleccionId = null;
  utils.toast('Producto eliminado del catálogo.');
  App.render.all();
};
