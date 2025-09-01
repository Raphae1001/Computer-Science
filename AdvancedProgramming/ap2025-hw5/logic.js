
function solution_js_query() {
    // Select all <a> elements with class 'malicious' inside the div with id 'q5'
    const maliciousLinks = document.querySelectorAll('#q5 .hidden a.malicious');
    
    // Hide all malicious links by setting display to none
    maliciousLinks.forEach(link => {
        link.setAttribute('style', 'display: none;');
    });

    // Select the hidden <div> inside the div with id 'q5'
    const hiddenDiv = document.querySelector('#q5 .hidden');
    if (hiddenDiv) {
        hiddenDiv.setAttribute('style', 'display: block;');
    }
}

function solution_js_dynamic_elements() {
    // Select the div with id 'q6'
    const q6Div = document.getElementById('q6');
    
    // Create a new div element
    const newDiv = document.createElement('div');
    
    // Create an h2 element with text
    const newH2 = document.createElement('h2');
    newH2.textContent = 'Dynamic Heading';
    
    // Create a p element with text
    const newP = document.createElement('p');
    newP.textContent = 'This is a dynamically added paragraph.';
    
    // Append h2 and p to the new div
    newDiv.appendChild(newH2);
    newDiv.appendChild(newP);
    
    // Append the new div to q6
    q6Div.appendChild(newDiv);
}

function solution_js_event_listeners() {
    // Add click event listener to div with ID 'div_btn'
    const divBtn = document.getElementById('div_btn');
    if (divBtn) {
        divBtn.addEventListener('click', () => {
            alert('click');
        });
    }

    // Add keydown event listener to body
    document.body.addEventListener('keydown', (event) => {
        alert(`The key '${event.key}' was pressed`);
    });
}

function solution_js_unit_converter() {
    const inputField = document.getElementById('convertion_input');
    const outputField = document.getElementById('convertion_output');
    const fromUnit = document.getElementById('convert_from_unit').value;
    const toUnit = document.getElementById('convert_to_unit').value;

    let value = parseFloat(inputField.value);
    if (isNaN(value)) {
        alert('Please enter a valid number');
        return;
    }

    const conversionRates = {
        cm: 1,
        meter: 100,
        inch: 2.54,
        foot: 30.48
    };

    if (conversionRates[fromUnit] && conversionRates[toUnit]) {
        let convertedValue = (value * (conversionRates[fromUnit] / conversionRates[toUnit]));
        outputField.value = convertedValue.toFixed(4);
    } else {
        alert('Invalid unit conversion');
    }
}




function check_username(username) {
    if (username.length<4){
        return false;
    }
    for (let i = 0; i < username.length; i++) { 
        let charCode = username.charCodeAt(i);
        if (
            !(
                (charCode >= 48 && charCode <= 57) || 
                (charCode >= 65 && charCode <= 90) || 
                (charCode >= 97 && charCode <= 122) || 
                charCode == 45 
            )
        ) { 
            return false; 
        }
    }
    return true;
}

function check_password(password){
    let passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*\-_()]).{8,}$/;
    if (!passwordRegex.test(password)) {
        return false;
    }
    return true;
}

function check_email(email){
    let emailRegex = /^(?!.*\.\.)\w([a-zA-Z0-9_.-]+\w+)*@([a-zA-Z0-9_-]+\.)+[a-zA-Z_]{2,}$/;
    if (!emailRegex.test(email)) {
        return false;
    }
    return true;
}

 function  validateForm() {
    // Get form values
    let username = document.getElementById("username").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let age = document.getElementById("age").value;
    let cities = document.getElementsByName("cities");
    let satisfaction = document.getElementById("satisfaction").value;
    let favColor = document.getElementById("fav_color").value; 

    var flag = true;
    if(!check_password(password)){
        flag=false;
    }
    if(!check_email(email)){
        flag=false
    }
    if(!check_username(username)){
        flag=false
    } 
    if (age < 10 || age > 120) {
        flag = false;
    }

    if(flag){
        alert("The form is valid")
        return
    }
    if(!flag){
        alert("The form is invalid")
        return
    }

}