const creditCard = document.querySelector(".credit-card");
const creditCardContainer = document.querySelector(".credit-card-container");
const paypal = document.querySelector(".paypal");
const paypalContainer = document.querySelector(".paypal-container");

creditCard.addEventListener("click", function () {

    paypalContainer.style.display = "none";

    if(creditCardContainer.style.display == "block"){
        creditCardContainer.style.display = "none";
    }else{
        creditCardContainer.style.display = "block";
    }

});


paypal.addEventListener("click", function () {

    creditCardContainer.style.display = "none";

    if(paypalContainer.style.display == "block"){
        paypalContainer.style.display = "none";
    }else{
        paypalContainer.style.display = "block";
    }

});