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

// get data from input form to display comments 

/**
 * Fetches comments from the server and adds them to the DOM
 */
async function getCommentsUsingAsyncAwait() { 
    
    const response = await fetch('/data');  
    const comments = await response.json()

    // fetch x comments from user input - how to set this here? 

    // iterate through comments and add to page to display

    // get from query first 
    // then get from input form

    // use paramater to limit comments ah 

    const commentsListElement = document.getElementById('locations-container');
    commentsListElement.innerHTML = ''; // resetting the html??
    commentsListElement.appendChild(
        createListElement('🌻 ' + locations[0]));

    // Build the list of history entries.
    const historyEl = document.getElementById('history');
    game.history.forEach((line) => {
      historyEl.appendChild(createListElement(line));
    });
    
}



/**
 * Gets hard-coded locations from the server and adds them to the DOM
 */
async function getLocationsUsingAsyncAwait() { 
    
    const response = await fetch('/data');  
    const locations = await response.json()

    const locationsListElement = document.getElementById('locations-container');
    locationsListElement.innerHTML = '';
    locationsListElement.appendChild(
        createListElement('🗻: ' + locations[0]));
    locationsListElement.appendChild(
        createListElement('🌊: ' + locations[1]));
    locationsListElement.appendChild(
        createListElement('🖥: ' + locations[2]));
    locationsListElement.appendChild(
        createListElement('🌻: ' + locations[3]));
    
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
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
  factContainer.innerText.style.font = "12px georgia";
}

/**
 * Gets a hard-coded "Hello Fatima" greeting from the server and adds it to the DOM
 */
async function getGreetingUsingAsyncAwait() { 
  const response = await fetch('/data');
  const greeting = await response.text();
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
  greetingContainer.innerText.style.color = "#FEE9E1";
  greetingContainer.innerText.style.font = "12px georgia";
  greetingContainer.innerText.padding = "10px";
}
