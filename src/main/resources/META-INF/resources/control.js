const CMD_BATTERY = 'CMD_BATTERY';
const CMD_MOTOR = 'CMD_MOTOR';
const CMD_SERVO = 'CMD_SERVO';
const CMD_BUZZER = 'CMD_BUZZER';

const KEY_ARROW_UP = 'ArrowUp';
const KEY_ARROW_DOWN = 'ArrowDown';
const KEY_ARROW_LEFT = 'ArrowLeft';
const KEY_ARROW_RIGHT = 'ArrowRight';
const KEY_W = 'w';
const KEY_S = 's';
const KEY_A = 'a';
const KEY_D = 'd';

let connected = false;
let socket;

const buzzerBtn = document.getElementById('buzzer');

const connectBtn = document.getElementById('connect');
const disconnectBtn = document.getElementById('disconnect');
const battery = document.getElementById('battery');
const webcam = document.getElementById('webcam');

const upBtn = document.getElementById('up');
const downBtn = document.getElementById('down');
const leftBtn = document.getElementById('left');
const rightBtn = document.getElementById('right');

const servoUpBtn = document.getElementById('servo-up');
const servoDownBtn = document.getElementById('servo-down');
const servoLeftBtn = document.getElementById('servo-left');
const servoRightBtn = document.getElementById('servo-right');
const servoHomeBtn = document.getElementById('servo-home');

const servo0Range = document.getElementById('servo0');
const servo1Range = document.getElementById('servo1');

[...document.getElementsByTagName('button')].forEach(btn => btn.toggleAttribute('disabled'));

const init = () => {
    [...document.getElementsByTagName('button')].forEach(btn => btn.toggleAttribute('disabled'));

    document.addEventListener('keydown', keyDownListener);
    document.addEventListener('keyup', keyUpListener);
    centerServos();
}

const connect = () => {
    if (!connected) {
        webcam.setAttribute('src', 'http://' + location.hostname + ':8000/?action=stream')
        socket = new WebSocket('ws://' + location.host + '/rc-car');
        socket.onopen = () => {
            connected = true;
            console.log('Connected to the web socket');
            init();
        };
        socket.onmessage = m => {
            console.log('Got message: ' + m.data);
            processMessage(m);
        };
        socket.onerror = e => {
            console.log('Error: ' + e);
            shutdown();
        }
        socket.onclose = e => {
            console.log('onClose: ' + e);
            shutdown();
        }
    }
};

const shutdown = () => {
    [...document.getElementsByTagName('button')].forEach(btn => btn.toggleAttribute('disabled'));
    webcam.setAttribute('src', '');
    document.removeEventListener('keydown', keyDownListener);
    document.removeEventListener('keyup', keyUpListener);
}

const disconnect = () => {
    if (connected) {
        connected = false;
        socket.close();
    }
}

function processMessage(m) {
    const data = m.data.split('#');
    if (data[0] === CMD_BATTERY) {
        const batteryPercent = (data[1] - 7) / 1.40 * 100;
        battery.setAttribute('value', batteryPercent.toString());
    }
}


const stopMotor = () => socket.send(CMD_MOTOR + '#0#0#0#0');
const setMotorForward = () => socket.send(CMD_MOTOR + '#2000#2000#2000#2000');
const setMotorReverse = () => socket.send(CMD_MOTOR + '#-2000#-2000#-2000#-2000');
const setMotorLeft = () => socket.send(CMD_MOTOR + '#2000#-2000#2000#-2000');
const setMotorRight = () => socket.send(CMD_MOTOR + '#-2000#2000#-2000#2000');

const sendServo0 = () => socket.send(CMD_SERVO + '#8#' + servo0Range.value);
const sendServo1 = () => socket.send(CMD_SERVO + '#9#' + servo1Range.value);

const centerServos = () => {
    servo0Range.value = 90;
    sendServo0()
    servo1Range.value = 90;
    sendServo1()
};

const increaseAndSetServo0 = () => {
    servo0Range.value = servo0Range.value * 1 + 1
    sendServo0();
};

const decreaseAndSetServo0 = () => {
    servo0Range.value -= 1;
    sendServo0();
};

const increaseAndSetServo1 = () => {
    servo1Range.value = servo1Range.value * 1 + 1
    sendServo1();
};

const decreaseAndSetServo1 = () => {
    servo1Range.value -= 1;
    sendServo1();
};

const keyDownListener = event => {
    console.log('Key down ' + event.key)
    switch (event.key) {
        case KEY_ARROW_UP:
            setMotorForward();
            break;
        case KEY_ARROW_DOWN:
            setMotorReverse();
            break;
        case KEY_ARROW_LEFT:
            setMotorLeft();
            break;
        case KEY_ARROW_RIGHT:
            setMotorRight();
            break;
        case KEY_A:
            decreaseAndSetServo0();
            break;
        case KEY_D:
            increaseAndSetServo0();
            break;
        case KEY_W:
            increaseAndSetServo1();
            break;
        case KEY_S:
            decreaseAndSetServo1();
            break;
    }
};

const keyUpListener = event => {
    console.log('Key up ' + event.key)
    switch (event.key) {
        case KEY_ARROW_UP:
        case KEY_ARROW_DOWN:
        case KEY_ARROW_LEFT:
        case KEY_ARROW_RIGHT:
            stopMotor();
            break;
    }
};


connectBtn.onclick = connect;
disconnectBtn.onclick = disconnect;

buzzerBtn.onmousedown = () => socket.send(CMD_BUZZER + '#ON')
buzzerBtn.onmouseup = () => socket.send(CMD_BUZZER + '#OFF')

upBtn.onmousedown = () => setMotorForward();
downBtn.onmousedown = () => setMotorReverse();
leftBtn.onmousedown = () => setMotorLeft()
rightBtn.onmousedown = () => setMotorRight()
upBtn.onmouseup = () => stopMotor()
downBtn.onmouseup = () => stopMotor()
leftBtn.onmouseup = () => stopMotor()
rightBtn.onmouseup = () => stopMotor()

servo0Range.onchange = () => sendServo0();
servo1Range.onchange = () => sendServo1();

servoHomeBtn.onclick = () => {
    centerServos();
}

servoUpBtn.onclick = () => increaseAndSetServo1();
servoDownBtn.onclick = () => decreaseAndSetServo1();
servoLeftBtn.onclick = () => decreaseAndSetServo0();
servoRightBtn.onclick = () => increaseAndSetServo0();
