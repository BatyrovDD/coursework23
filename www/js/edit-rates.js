//обновление тарифа, формируем данные и отправляем запрос
function updateRates(tokenCookie){

    let ratesId = $(".rates__selector").val();

    let specifiedSingleRatePrice = $(".single__glazing__price").val();
    let specifiedDailyRatePrice= $(".double__glazing__price").val();
    let specifiedNightRatePrice = $(".triple__glazing__price").val();

    //проверяем пустые поля
    isEmpty(ratesId) && throwUserInputError();
    isEmpty(specifiedSingleRatePrice) && throwUserInputError();
    isEmpty(specifiedDailyRatePrice) && throwUserInputError();
    isEmpty(specifiedNightRatePrice) && throwUserInputError();

    $.ajax({
        url: '/api/edit_prices',     
        type: 'GET',
        data : {
            token : tokenCookie,
            rates_set_id: ratesId,
            single_rate_price : specifiedSingleRatePrice,
            daily_rate_price: specifiedDailyRatePrice,
            night_rate_price: specifiedNightRatePrice,
            table_id: ratesId
        },
        dataType: 'json',                   
        success: function(data)
        {
      		//
        },
        statusCode: {
        	403: function (response) {
         		alert("Доступ запрещён");
      	}
        },
    })

}

//слушатели

$('.rates__selector').change(function() {
    getDataForEditForm();
});

let tokenCookie = getCookie("token"); 
console.log(tokenCookie);

$('#rates_form').change(function() {
    updateRates(tokenCookie);
    return false;
});

getDataForEditForm();