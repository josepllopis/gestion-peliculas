const params = new URLSearchParams(window.location.search);
const username = decodeURIComponent(window.location.pathname.split("/").pop());

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




    const request = await fetch (`/api/v1/films/user/${username}`,{
        method: 'GET',
        headers: getHeaders()
    });

    const pelis = await request.json();

    let peliculas = '';

    for(let peli of pelis){


        let agregarPelicula = '<tr><td>'+peli.nombre+'</td><td>'+peli.pais+'</td><td>'+peli.cinema+'</td><td>'+peli.fecha+'</td><td>'+peli.duracion+'</td><td>'+peli.puntuacion+'</td><td>'+peli.director+'</td></tr>'
        peliculas+=agregarPelicula;
    }

document.querySelector('#peliculas tbody').innerHTML = peliculas;

}

async function ordenarPeliculasBackend() {
  const criterio = document.getElementById('lista-orden').value; // ej: 'duracion'
  const orden = document.getElementById('lista-mayor').value;    // 'ASC' o 'DESC'

  try {
    const response = await fetch(`/api/v1/films/user/${username}/sorted?sortBy=${criterio}&direction=${orden}`);
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

            let agregarPelicula = '<tr><td>'+peli.nombre+'</td><td>'+peli.pais+'</td><td>'+peli.cinema+'</td><td>'+peli.fecha+'</td><td>'+peli.duracion+'</td><td>'+peli.puntuacion+'</td><td>'+peli.director+'</td></tr>'
            peliculas+=agregarPelicula;
        }

    document.querySelector('#peliculas tbody').outerHTML = peliculas;
}

selectOrden.addEventListener('change', ordenarPeliculasBackend);
selectDireccion.addEventListener('change', ordenarPeliculasBackend);



