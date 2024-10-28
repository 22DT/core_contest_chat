const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

let currentSubscription = null; // 현재 구독을 저장하는 변수

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.connectHeaders={
        userId: $("#userId").val() // 커스텀 헤더로 userId 추가
    }

    stompClient.activate(); // WebSocket 연결 활성화
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
    if (currentSubscription) {
        currentSubscription.unsubscribe(); // 구독 해제
        currentSubscription = null; // 구독 변수 초기화
    }
}

function subscribe() {
    const destination = $("#destination").val(); // 입력된 구독 경로

    currentSubscription = stompClient.subscribe(
        destination,
        (message) => {
            // 메시지의 본문을 파싱해서 ChatMessage 로 변환
            const chatMessage = JSON.parse(message.body);

            // name: content 형식으로 메시지 출력
            showGreeting(chatMessage.name + ": " + chatMessage.content);
        },
        { userId: $("#userId").val() } // userId를 헤더로 추가
    );
}




function unsubscribe() {
    const subscriptionId = $("#subscribeId").val(); // 원하는 subscription ID를 여기에 설정하세요.
    stompClient.unsubscribe(subscriptionId)
}

function sendMessage() {
    const destination = $("#send_destination").val();
    const message = {
        roomId: $("#roomId").val(),
        name: $("#name").val(),
        content: $("#content").val()
    };

    stompClient.publish({
        header:{
          name: $("#name").val()
        },
        destination: destination,
        body: JSON.stringify(message),
    });
}


function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#subscribe").click(() => subscribe());
    $("#send").click(() => sendMessage());
    $("#unsubscribe").click(() => unsubscribe()); // 구독 취소 버튼 클릭 이벤트 추가
});
