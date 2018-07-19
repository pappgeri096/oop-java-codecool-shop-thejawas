function togglePaymentOptions() {

    const creditCardContainer = document.querySelector(".credit-card-container");
    const paypalContainer = document.querySelector(".paypal-container");
    const payContainer = document.querySelector(".pay-container");

    payContainer.addEventListener("click", function (e) {


        if(e.target.classList.contains("paypal")){
            creditCardContainer.style.display = "none";

            if(paypalContainer.style.display == "block"){
                paypalContainer.style.display = "none";
            }else{
                paypalContainer.style.display = "block";
            }
        }

        // if(e.target.classList.contains("credit-card")){
        //     paypalContainer.style.display = "none";
        //
        //     if(creditCardContainer.style.display == "block"){
        //         creditCardContainer.style.display = "none";
        //     }else{
        //         creditCardContainer.style.display = "block";
        //     }
        //
        // }



    });
}


togglePaymentOptions();
