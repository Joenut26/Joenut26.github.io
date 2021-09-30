const httpRequest = new XMLHttpRequest();
const url = "http://localhost:8080/fifaPlauti";
//const url = "https://joenut26.github.io/fifaApp/"

let teams = [];

//httpRequest.onload() = getGames();



function getGames(){
    httpRequest.open('GET', url, true);

    httpRequest.setRequestHeader("Content-Type", "application/json");
    httpRequest.setRequestHeader("Accept", "application/json");

    httpRequest.send();

    console.log('yooo');
}

function submit(){
    console.log('submitting data')
    let gameUrl = url + "/gameForm";
    httpRequest.open('POST', gameUrl, true);

    httpRequest.setRequestHeader("Content-Type", "application/json");
    httpRequest.setRequestHeader("Accept", "application/json");

    let input = this.getInput();
    console.log(JSON.stringify(input));

    httpRequest.send(JSON.stringify(input));
}

function reset(){

    let inputFields = Array.from(document.getElementsByClassName("input"));
    inputFields.forEach(input => input.value = "");
}

function getInput(){

    let playerOne = document.getElementById("player1").value;
    let playerTwo = document.getElementById("player2").value;
    let goalsOne = document.getElementById("goals1").value;
    let goalsTwo = document.getElementById("goals2").value;
    let rankingOne = document.getElementById("rank1").value;
    let rankingTwo = document.getElementById("rank2").value;
    let teamOne = document.getElementById("team1").value;
    let teamTwo = document.getElementById("team2").value;

    let input = {};
    input["teamOne"] = {rank: rankingOne, name: teamOne, goals: goalsOne, player: playerOne};
    input["teamTwo"] = {rank: rankingTwo, name: teamTwo, goals: goalsTwo, player: playerTwo};

    let filled = [];
    for(let team of Object.values(input)){

        if(Object.values(team).includes("") || Object.values(team.player).includes("")){

            filled.push(false);
        }

    }

    console.log(JSON.stringify(input));
    let isBlank = filled.includes(false);
    console.log(isBlank);

    if(isBlank){
       alert("Please make sure everything is filled");
    } else {
        return input;
    }



}