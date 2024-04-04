const baseUrl = '/api/v1'

const ws = Stomp.client("ws://localhost:8080/chatroom");
ws.connect({}, () => {}, () => {}, () => alert('connection closed, please refresh'))

function getRequest(method, body) {
  let request = { method: method }
  if (body) {
    request.headers = { "Content-Type": "application/json" };
    request.body = JSON.stringify(body);
  }
  return request
}

let subscription = null;
function subscribe(room, messageListener) {
  if (subscription) {
    subscription.unsubscribe();
  }
  subscription = ws.subscribe("/topic/" + room, (message) => messageListener(JSON.parse(message.body)));
}

function send(room, username, text) {
  return fetch(`${baseUrl}/rooms/${room}/messages`, getRequest("POST", { username, text }))
}

function getRooms(roomListener) {
  fetch(`${baseUrl}/rooms`, getRequest("GET"))
    .then(response => response.json())
    .then(json => json.forEach(roomListener));
}

function getRoomHistory(room, messageListener) {
  fetch(`${baseUrl}/rooms/${room}/messages`, getRequest("GET"))
    .then(response => response.json())
    .then(json => json.forEach(messageListener));
}

const messageInput = document.getElementById("messageInput");
const sendButton = document.getElementById("sendButton");
const usernameInput = document.getElementById("usernameInput");
const chatroomSelect = document.getElementById("chatroomSelect");
const messagesDiv = document.getElementById("messages");
const roomNameH1 = document.getElementById("roomName");
const roomDiv = document.getElementById("room");

function clearInput() {
  messageInput.value = "";
  messageInput.focus();
}

function onSendError() {
  alert("error sending the message");
}

function onMessageSent(response) {
  if (!response.ok) {
    onSendError();
  } else {
    clearInput()
  }
}

function div(clazz, content) {
  const element = document.createElement("div");
  element.classList.add(clazz);
  element.textContent = content // textContent strips html tags
  return element
}

function displayMessage(message) {
  const messageDiv = div("message");

  messageDiv.appendChild(div("timestamp", new Date(message.posted).toLocaleTimeString()))
  messageDiv.appendChild(div("username", message.username))
  messageDiv.appendChild(div("text", message.text))

  const user = usernameInput.value.trim()

  if (user == message.username) {
    messageDiv.classList.add("own");
  }

  if (user && message.text.includes(user)) {
    messageDiv.classList.add("mention");
  }

  messagesDiv.appendChild(messageDiv);
  messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function addRoomOption(room) {
  var option = document.createElement('option');
  option.value = room.name;
  option.innerHTML = room.label;
  chatroomSelect.appendChild(option);
}

function sendMessage() {
  const text = messageInput.value.trim();
  if (text === "") {
    return;
  }
  const username = usernameInput.value.trim() || "Anonymous";
  const room = chatroomSelect.value;

  send(room, username, text)
    .then(onMessageSent)
    .catch(onSendError);
}

function displayRoom(roomName) {
  messagesDiv.innerHTML = "";
  roomNameH1.innerHTML = roomName;
  roomDiv.style.display = "block";
}

function openRoom(room, roomName) {
  displayRoom(roomName);
  subscribe(room, displayMessage);
  getRoomHistory(room, displayMessage);
}

sendButton.addEventListener("click", sendMessage)

messageInput.addEventListener("keyup", function(event) {
  if (event.keyCode === 13) { // Enter
    sendMessage();
  }
});

chatroomSelect.addEventListener("change", function() {
  if (this.value) {
    openRoom(this.value, this.selectedOptions[0].text)
  }
});

getRooms(addRoomOption)