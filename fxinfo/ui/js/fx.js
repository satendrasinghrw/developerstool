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
  xmlhttp.open("POST", "http://192.168.10.89:8080/fx/signin", true);
  xmlhttp.setRequestHeader("Content-type", "Application/JSON");
  xmlhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      console.log(this.responseText);
    }
  };
  xmlhttp.send(
    JSON.stringify({
      password: "",
      userName: ""
    })
  );
}

function changehtml(htmlobject) {
  console.log(htmlobject);
}
