// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// /**
//  * Adds four locations that Fatima has lived in to the page.
//  */
// function addLocations() {
//     fetch('/data').then(response => response.json()).then((locations) => {
//     // locations is a JSON object from the JSON string
//     // response is a JSON string, locations object
//     console.log('Fetching a random quote.');
//     console.log(response);

//     //const locationsListElement = document.getElementById('locations-container');
//     });

// }
/**
 * Gets hard-coded locations from the server and
  adds them to the DOM
 */
async function getLocationsUsingAsyncAwait() { 
    
    const response = await fetch('/data');  
    const locations = await response.json()
    console.log('Fetching locations.');
    console.log(locations);

    const locationsListElement = document.getElementById('locations-container');
    locationsListElement.innerText = locations;
    // locationsListElement.innerHTML = '';
    // locationsListElement.appendChild(
    //     createListElement('1: ' + locations.get(0)));
    // locationsListElement.appendChild(
    //     createListElement('2: ' + locations.get(1)));
    // locationsListElement.appendChild(
    //     createListElement('3: ' + locations.get(2)));
    // locationsListElement.appendChild(
    //     createListElement('4: ' + locations.get(3)));
    
    //     response.setContentType("application/json;");
    // response.getWriter().println(json);
  //document.getElementById('greeting-container').innerText = greeting;
}



/**
 * Adds a random fact about Fatima to the page.
 */
function addRandomFact() {
  const fatimaFacts =
      ['I am left handed!', 'I have an older brother named Humza!', 
      'My favorite color is turquoise!', 'I love photography!',
      'I would love to visit Japan someaday!', 'Growing up, I played soccer for four years!'];

  // Pick a random greeting.
  const fact = fatimaFacts[Math.floor(Math.random() * fatimaFacts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('random-fact-container');
  factContainer.style.color = "#023C40";
  factContainer.innerText = fact;
  factContainer.innerText.style.color = "#FEE9E1";
  factContainer.innerText.style.font = "20px georgia";
}


/**
 * Gets a hard-coded "Hello Fatima" greeting from the server and adds it to the DOM
 */
async function getGreetingUsingAsyncAwait() { 
  const response = await fetch('/data');
  const greeting = await response.text();
  document.getElementById('greeting-container').innerHTML = greeting;
}
