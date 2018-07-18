document.querySelector(".credit-card").addEventListener("click", function () {

    if(document.querySelector(".credit-card-container").style.display == "block"){
        document.querySelector(".credit-card-container").style.display = "none";
    }else{
        document.querySelector(".credit-card-container").style.display = "block";
    }

});