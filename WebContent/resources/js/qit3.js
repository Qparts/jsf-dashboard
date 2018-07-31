/**
 * 
 */


function openOrderTab(tabName) {
    var i;
    var x = document.getElementsByClassName("order-tab");
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";  
    }
    document.getElementById(tabName).style.display = "block";  
}

function handleDrop(event, ui) {
    var droppedCar = ui.draggable;
    droppedCar.fadeOut('fast');
}


function resetActive(event, percent, step) {
    $(".progress-bar").css("width", percent + "%").attr("aria-valuenow", percent);
    $(".progress-completed").text(percent + "%");
    $("div").each(function () {
        //if ($(this).hasClass("activestep")) {
          //  $(this).removeClass("activestep");
        //}
    });

    if (event.target.className == "col-md-2") {
       // $(event.target).addClass("activestep");
    }
    else {
        //$(event.target.parentNode).addClass("activestep");
    }

    hideSteps();
    showCurrentStepInfo(step);
}


function hideSteps() {
    $("div").each(function () {
        if ($(this).hasClass("activeStepInfo")) {
            $(this).removeClass("activeStepInfo");
            $(this).addClass("hiddenStepInfo");
        }
    });
}

function showCurrentStepInfo(step) {        
    var id = "#" + step;
    $(id).addClass("activeStepInfo");
}

$(document).ready(function(){
    $("#myBtn").click(function(){
        $("#myModal").modal();
    });
	
	  $("#myBtn2").click(function(){
        $("#myModal2").modal();
    });
	
	 $("#myBtn3").click(function(){
        $("#myModal3").modal();
    });
	 $("#myBtn4").click(function(){
		 doLogout();
	    });
	 $("#myBtn5").click(function(){
		 $("#myModal3").modal("toggle");
		 $("#myModal").modal();
	    });
	 $("#myBtn6").click(function(){
		 $("#myModal3").modal("toggle");
		 $("#myModal2").modal();
	    });
});

function hideModalAndShowLogin(){
	$("#myModal3").modal("toggle");
	 $("#myModal").modal();
}

function hideModalAndShowRegister(){
	$("#myModal3").modal("toggle");
	 $("#myModal2").modal();
}

function showRegisterDialog(){
	$("#myModal2").modal();
}

function showCompleteDialog(){
	$("#completeModal").modal();
}

function showCartDialog(){
	$("#myModal3").modal();
}


var __dcid = __dcid || [];__dcid.push(["DigiCertClickID_Bs0_36Wa", "3", "s", "black", "Bs0_36Wa"]);
(function(){
	var cid=document.createElement("script");
	cid.async=true;
	cid.src="//seal.digicert.com/seals/cascade/seal.min.js";
	var s = document.getElementsByTagName("script");
	var ls = s[(s.length - 1)];
	ls.parentNode.insertBefore(cid, ls.nextSibling);
	}());



