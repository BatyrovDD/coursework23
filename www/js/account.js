//заполнение списка созданных пользователем отчётов
function formPdfReportsPlace(userId){
	$.ajax({ 
		type : 'GET', 
		url : '/api/get_user_reports', 
		async : false, 
		data : {
			ownerId : userId
		},
		dataType : 'json', 
		success : function(result){
			let reportCount = 1;
			$.each(result.responseBody, function(index, val){
				let uri = "https://project23.usatu.su"; 
				let docUrl;
				docUrl = uri + val;
				let token = readCookie('token');
				$(".pdf__reports__place").append('<li id="report__li__' + reportCount + '"><a href=' + docUrl +'>Отчёт ' + reportCount + '</a><a onclick="deletePdfReport(\'' + token + '\', \'' + val + '\', ' + reportCount + ')" title="Удалить отчёт">❌</a></li>');
				reportCount++;
			});
		},
		error : function(error){
			$(".pdf__reports__place").append('Пока вы не сделали ни одного отчёта');
		}
	});
}

function deletePdfReport(token, documentName, liNumber){
	$.ajax({ 
		type : 'GET', 
		url : '/api/delete_pdf_report', 
		async : false, 
		data : {
			token : token,
			documentName: documentName
		},
		dataType : 'json', 
		success : function(result){
			deleteLi(liNumber);
			//console.log(liNumber);
		},
		statusCode: {
			404: function (response) {
				console.log(response);
				alert("Не удалось удалить");
			},
			500: function (response) {
				alert("Ошибка сервера");
			}
		}
	});
}

function deleteLi(liNumber){
	let liId = '#report__li__' + liNumber;
	$(liId).remove();
	if (liNumber === 1) $(".pdf__reports__place").append('Пока вы не сделали ни одного отчёта');
}

let tokenCookie = getCookie("token"); 
//получение информации о пользователе
let user;
if (tokenCookie) {
	user = getUserInfo(tokenCookie);
	$('.edit__username__input').val(user.username);
	$('.edit__fullname__input').val(user.fullName);
	$('.edit__email__input').val(user.email);
} else {
	//редиректим если нет кук
	window.location.href = "../";
}
//слушатель нажатия кнопки внесения изменений
$('.edit__account__button').click({token: tokenCookie}, function(event) { //https://stackoverflow.com/questions/3273350/jquerys-click-pass-parameters-to-user-function
	let username = $('.edit__username__input').val();
	let fullName = $('.edit__fullname__input').val();
	let email = $('.edit__email__input').val();
	let password = $('.edit__password__input').val();
	let repeatedPassword = $('.edit__repeat__password__input').val();

	if (username === "" || fullName === "" || email === ""){
		alert ("Значения полей не могут быть пустыми");
	} else {
		let passwordsAreEqual = false;
		if (password === repeatedPassword) {
			passwordsAreEqual = true;
		} else {
			alert ("Пароли не совпадают");
		}


		if (passwordsAreEqual) {
			if (password === "") password = "111111111"; // при вводе '111111111' пароль не обновится
			$.ajax({
				url: '/api/update_profile',     
				type: 'POST',
				data : {
					token : event.data.token,
					email : email,
					fullName : fullName,
					password : password
				},
				dataType: 'json',
				success: function(data)
				{
					let newToken = data.responseBody.apiToken;
					setCookie("token", newToken);
					alert("Данные обновлены");
				},
				statusCode: {
					404: function (response) {
						alert("Не найден пользователь для редактирования");
					},
					500: function (response) {
						alert("При обновлении произошла ошибка");
					}
				},
			})
		}

	}
});
//подгружаем список pdf отчетов пользователя
formPdfReportsPlace(user.id);
//если админ - открываем блок с админскими функциями
if (user.groupId === 1) $('.admin__zone').show();