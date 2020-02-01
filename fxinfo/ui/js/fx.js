function sendSignUpRequest() {
  var xmlhttp = new XMLHttpRequest();
  xmlhttp.open("POST", "http://192.168.10.89:8080/login/signup", true);
  xmlhttp.setRequestHeader("Content-type", "Application/JSON");
  xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      console.log(this.responseText);
    }
  };
  var firstName = document.getElementById("firstName").value;
  var lastName = document.getElementById("lastName").value;
  var emailId = document.getElementById("emailId").value;
  var password = document.getElementById("password").value;
  var mobileNumber = document.getElementById("mobileNumber").value;
  var companyName = document.getElementById("companyName").value;
  console.log(firstName);
  xmlhttp.send(
    JSON.stringify({
      firstName: firstName,
      lastName: lastName,
      emailId: emailId,
      password: password,
      mobileNumber: mobileNumber,
      companyName: companyName
    })
  );
}

function fxtest() {
  var xmlhttp = new XMLHttpRequest();
  xmlhttp.open(
    "GET",
    "http://192.168.10.89:8080/swagger-ui.html#!/fx-info-controller/getSpotRateUsingGET",
    true
  );
  xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      //   var htmlstring = this.responseText;
      //   var doc = new DOMParser().parseFromString(htmlstring, "text/html");
      //   changehtml(doc);
      console.log(this.responseText);
    }
  };
  xmlhttp.send();
}

function validateFXLogin() {
  var xmlhttp = new XMLHttpRequest();
  xmlhttp.open("POST", "http://192.168.10.89:8080/login/signin", true);
  xmlhttp.setRequestHeader("Content-type", "Application/JSON");
  xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      console.log(this.responseText);
      var accessToken = this.responseText.substring(
        this.responseText.indexOf("[") + 1,
        this.responseText.indexOf("]") - 1
      );
      document.cookie = accessToken;
      console.log(document.cookie);
      var element = document.getElementById("loginId");
      console.log(element);
      element.innerHTML = "Sign Out";
      console.log(element);
      document.getElementById("signUp").remove();
    }
  };
  xmlhttp.send(
    JSON.stringify({
      password: document.getElementById("Password").value,
      userName: document.getElementById("UserName").value
    })
  );
}

function changehtml(htmlobject) {
  console.log(htmlobject);
}
