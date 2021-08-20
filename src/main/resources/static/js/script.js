const httpRequest = new XMLHttpRequest();
//const url = "http://localhost:8080/fifaPlauti";
const url = "https://joenut26.github.io/fifaApp/"

let teams = [];



function submit(){
    console.log('lmao')
    let gameUrl = url + "/gameForm";
    httpRequest.open('POST', gameUrl, true);

    httpRequest.setRequestHeader("Content-Type", "application/json");
    httpRequest.setRequestHeader("Accept", "application/json");

    let input = this.getInput();
    console.log(JSON.stringify(input));

    httpRequest.send(JSON.stringify(input));
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
    input["teamOne"] = {rank: rankingOne, name: teamOne, goals: goalsOne, player: {name: playerOne}};
    input["teamTwo"] = {rank: rankingTwo, name: teamTwo, goals: goalsTwo, player: {name: playerTwo}};

    console.log(JSON.stringify(input));
//    let isFilled = inputList.every(input =>
//        input.value != null || input.value !=""
//    )
//    console.log(isFilled);

//    if(!isFilled){
//        alert("Please make sure everything is filled");
//    } else {
        return input;
//    }



}