window.App = window.App || {};
App.render = App.render || {};
App.actions = App.actions || {};

App.render.categorias = function(){
  const { state, utils } = App;
  document.getElementById('app').innerHTML = `
    <div class="panel-head">
      <div><div class="path">Menú principal</div><h2>Gestionar categorías</h2></div>
    </div>
    <div class="tag-list">
      ${state.categorias.map(c => {
        const enUso = state.productos.some(p => p.categoria === c);
        return `<span class="tag">${utils.esc(c)} ${enUso ? '' : `<button data-action="cat-delete" data-cat="${utils.esc(c)}" title="Eliminar categoría">✕</button>`}</span>`;
      }).join('')}
    </div>
    <form class="stack" data-form="agregar-categoria" style="flex-direction:row; align-items:flex-end;">
      <div class="field" style="flex:1; margin:0;">
        <label>Nueva categoría</label>
        <input type="text" name="nombre" maxlength="40" placeholder="Ej: Guantes">
      </div>
      <button type="submit" class="btn">Agregar categoría</button>
    </form>
    <div class="hint" style="margin-top:8px; color:var(--ink-faint); font-size:11px;">
      Las categorías que ya tienen productos asignados no se pueden eliminar.
    </div>`;
};

App.actions.agregarCategoria = function(nombre){
  const { state, utils } = App;
  nombre = (nombre || '').trim();
  if(!nombre){ utils.toast('Escribí un nombre de categoría.', 'error'); return; }
  if(state.categorias.some(c => c.toLowerCase() === nombre.toLowerCase())){
    utils.toast('Esa categoría ya existe.', 'error'); return;
  }
  state.categorias.push(nombre);
  utils.toast(`Categoría "${nombre}" agregada.`);
  App.render.all();
};

App.actions.eliminarCategoria = function(nombre){
  App.state.categorias = App.state.categorias.filter(c => c !== nombre);
  App.render.all();
};
