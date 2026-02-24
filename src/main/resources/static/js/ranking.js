$(document).ready(function() {

    cargarRanking();

});

function getHeaders(){
    return {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        };
}

async function cargarRanking(){

    alert('Capullo')

    const request = await fetch ('/api/v1/ranking',{
        method: 'GET',
        headers: getHeaders()
    });





    const ranking = await request.json();

    const rankingList = document.getElementById("rankingList");
    rankingList.innerHTML = ""; // limpiamos contenido anterior

    if (!ranking || ranking.length === 0) {
        rankingList.innerHTML = "<p>No hay datos todavía</p>";
        return;
    }

    ranking.forEach((usuario, index) => {

        let claseExtra = "";
        let posicionVisual = index + 1;

        if(index === 0) {
            claseExtra = "first";
            posicionVisual = "🥇";
        } else if(index === 1) {
            claseExtra = "second";
            posicionVisual = "🥈";
        } else if(index === 2) {
            claseExtra = "third";
            posicionVisual = "🥉";
        }

        const li = document.createElement("li");
        li.className = `ranking-item ${claseExtra}`;

        li.innerHTML = `
            <span class="ranking-position">${posicionVisual}</span>
            <span class="ranking-name">${usuario.username.toUpperCase()}</span>
            <span class="ranking-count">${usuario.totalPeliculas} películas</span>
        `;

        rankingList.appendChild(li);
    });
    }