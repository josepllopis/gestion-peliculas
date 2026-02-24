
// Obtener parámetros de la URL (por ejemplo ?id=12)
const params = new URLSearchParams(window.location.search);
const id = params.get('id'); // null si no existe

const titleEl = document.getElementById('form-title');
const titleh1 = document.getElementById('form-h1');
const btnGuardar = document.getElementById('enviar');

const method = id ? 'PUT' : 'POST';
const url = id ? `/api/v1/films/${id}` : '/api/v1/films';

const inputNombre = document.getElementById('nombre');
const inputPais = document.getElementById('lista-paises');
const inputCinema = document.getElementById('cinema');
const inputFecha = document.getElementById('fecha');
const inputDuracion = document.getElementById('duracion');
const inputPuntuacion = document.getElementById('puntuacion');
const spanPuntuacion = document.getElementById('valorPuntuacion');
const inputDirector = document.getElementById('director');



actualizarPelicula(id)

async function actualizarPelicula(id){

if(id){

      const request = await fetch (`/api/v1/films/${id}`,{
            method: 'GET',
            headers: getHeaders()
        });

      const peli = await request.json();


    let nombrePelicula = peli.nombre;
    let paisPelicula = peli.pais;
    let cinePelicula = peli.cinema;
    let fechaPelicula = peli.fecha;
    let duracionPelicula = peli.duracion;
    let puntuacionPelicula = peli.puntuacion;
    let directorPelicula = peli.director;



    const [dia, mes, anio] = fechaPelicula.split('/'); // separamos por "/"
    const fechaFormateada = `${anio}-${mes}-${dia}`;

    titleEl.textContent = 'Editar Película';
    titleh1.textContent = 'Editar Película';
    btnGuardar.textContent = 'Actualizar';

    inputNombre.value = nombrePelicula;
    inputPais.value = paisPelicula;
    inputCinema.value = cinePelicula;
    inputFecha.value = fechaFormateada;
    inputDuracion.value = Number(duracionPelicula);
    inputPuntuacion.value = puntuacionPelicula;
    spanPuntuacion.textContent = puntuacionPelicula;
    inputDirector.value = directorPelicula;
}else{

}

}



document.addEventListener("DOMContentLoaded", function() {
const form = document.getElementById("formPeliculas");





form.addEventListener("submit", function(event) {
  event.preventDefault(); // Evita recarga

  // Validación automática de HTML5
  if (!form.checkValidity()) {
    form.reportValidity(); // Muestra mensaje estándar del navegador
    return;
  }

  // Capturar los datos del formulario
  let nombre = document.getElementById("nombre").value;
  let pais = document.getElementById("lista-paises").value;
  let cine = document.getElementById("cinema").value;
  let fecha = document.getElementById("fecha").value;
  let partes = fecha.split("-"); // ["2025", "10", "17"]
  let fechaFormateada = partes[2] + "/" + partes[1] + "/" + partes[0];
  let duracion = document.getElementById("duracion").value;
  let puntuacion = document.getElementById("puntuacion").value;
  let director = document.getElementById("director").value;




  agregarPelicula(nombre,pais,cine,duracion,director,puntuacion,fechaFormateada)

  // Redirigir a otra página
  window.location.href = "peliculas"; // Cambia esto por la URL que quieras
});
});

async function agregarPelicula(nombre,pais,cine,duracion,director,puntuacion,fechaFormateada){



  let datos = {}

  datos.nombre = nombre;
  datos.pais = pais;
  datos.cinema = cine;
  datos.duracion = duracion;
  datos.director = director;
  datos.puntuacion = puntuacion;
  datos.fecha = fechaFormateada;

   const request = await fetch(url, {
      method: method,
      headers: getHeaders(),
      body: JSON.stringify(datos)
    });
    window.location.href = "http://localhost:8096/peliculas";
}

function getHeaders(){
    return {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        };
}