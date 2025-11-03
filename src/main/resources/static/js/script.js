$(document).ready(function() {

    cargarPeliculas();

});

const selectOrden = document.getElementById('lista-orden');
const selectDireccion = document.getElementById('lista-mayor');

function getHeaders(){
    return {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        };
}

async function cargarPeliculas(){


    const request = await fetch ('/api/v1/films',{
        method: 'GET',
        headers: getHeaders()
    });

    const pelis = await request.json();

    let peliculas = '';

    for(let peli of pelis){
        let botones = `<a href="#"  onclick="eliminarPelicula(${peli.id})" style="margin-right:10px" class="btn btn-danger btn-circle" ><i class="fas fa-trash"></i></a><a href="#" onclick="actualizarPelicula(${peli.id})" class="btn btn-warning btn-circle"><i class="fas fa-pen"></i></a>`

        let agregarPelicula = '<tr><td>'+peli.nombre+'</td><td>'+peli.pais+'</td><td>'+peli.cinema+'</td><td>'+peli.fecha+'</td><td>'+peli.duracion+'</td><td>'+peli.puntuacion+'</td><td>'+peli.director+'</td><td>'+botones+'</td></tr>'
        peliculas+=agregarPelicula;
    }

document.querySelector('#peliculas tbody').innerHTML = peliculas;






}

async function eliminarPelicula(id) {


   Swal.fire({
     title: 'Eliminar Película',
     text: '¿Estás seguro de que deseas eliminar esta película?',
     icon: 'warning',
     confirmButtonText: 'Eliminar',
     cancelButtonText: 'Cancelar',
     showCancelButton: true,
     showLoaderOnConfirm: true,
     allowOutsideClick: () => !Swal.isLoading(),
     preConfirm: async () => {
       try {
         const response = await fetch(`/api/v1/films/${id}`, {
           method: 'DELETE',
           headers: getHeaders()
         });

         if (!response.ok) {
           throw new Error('Error al eliminar la película');
         }

         return true; // indica éxito a Swal
       } catch (error) {
         Swal.showValidationMessage(`❌ ${error.message}`);
         return false;
       }
     }
   }).then(result => {
     if (result.isConfirmed && result.value) {
       Swal.fire('Éxito', 'Película eliminada correctamente', 'success');
       cargarPeliculas();
     }
   });
}

function actualizarPelicula(id){
     window.location.href = `agregarPelicula.html?id=${id}`;
}

async function ordenarPeliculasBackend() {
  const criterio = document.getElementById('lista-orden').value; // ej: 'duracion'
  const orden = document.getElementById('lista-mayor').value;    // 'ASC' o 'DESC'

  try {
    const response = await fetch(`/api/v1/films/sorted?sortBy=${criterio}&direction=${orden}`);
    if (!response.ok) throw new Error('Error al obtener películas ordenadas');


        if (response.status === 204) {
                Swal.fire('Aviso', 'No hay películas para ordenar.', 'info');
                return;
            }
    const peliculasOrdenadas = await response.json();
    // recarga la tabla con los datos ordenados
    cargarPeliculasOrdenadas(peliculasOrdenadas);

  } catch (err) {
    console.error(err);
    Swal.fire('Error', 'No se pudo ordenar la lista de películas', 'error');
  }
}

function cargarPeliculasOrdenadas(peliculasOrdenadas){

    let peliculas = '';

    for(let peli of peliculasOrdenadas){
            let botones = `<a href="#"  onclick="eliminarPelicula(${peli.id})" style="margin-right:10px" class="btn btn-danger btn-circle" ><i class="fas fa-trash"></i></a><a href="#" onclick="actualizarPelicula(${peli.id})" class="btn btn-warning btn-circle"><i class="fas fa-pen"></i></a>`

            let agregarPelicula = '<tr><td>'+peli.nombre+'</td><td>'+peli.pais+'</td><td>'+peli.cinema+'</td><td>'+peli.fecha+'</td><td>'+peli.duracion+'</td><td>'+peli.puntuacion+'</td><td>'+peli.director+'</td><td>'+botones+'</td></tr>'
            peliculas+=agregarPelicula;
        }

    document.querySelector('#peliculas tbody').outerHTML = peliculas;
}

selectOrden.addEventListener('change', ordenarPeliculasBackend);
selectDireccion.addEventListener('change', ordenarPeliculasBackend);

async function descargarPDF(){

      const criterioDownload = document.getElementById('lista-orden').value;
      const ordenDownload = document.getElementById('lista-mayor').value;

      const response = await fetch(`/api/v1/films/pdf?sortBy=${criterioDownload}&direction=${ordenDownload}`);

                   if (response.status === 204) {
                       Swal.fire('Aviso', 'No hay películas para descargar.', 'info');
                       return;
                   }

 Swal.fire({
     title: 'Descargar Tabla',
     text: '¿Estás seguro de que deseas descargar la lista de películas?',
     icon: 'info',
     confirmButtonText: 'Descargar',
     cancelButtonText: 'Cancelar',
     showCancelButton: true,
     showLoaderOnConfirm: true,
     allowOutsideClick: () => !Swal.isLoading(),
     preConfirm: async () => {
       try {

             const blob = await response.blob();
             const url = window.URL.createObjectURL(blob)

             const a = document.createElement('a');
             a.href = url;
             a.download = 'peliculas.pdf';
             document.body.appendChild(a);
             a.click();
             a.remove();

             window.URL.revokeObjectURL(url);

         if (!response.ok) {
           throw new Error('Error al eliminar la película');
         }

         return true; // indica éxito a Swal
       } catch (error) {
         Swal.showValidationMessage(`❌ ${error.message}`);
         return false;
       }
     }
   });


}