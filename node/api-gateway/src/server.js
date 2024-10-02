const express = require('express');
const bodyParser = require('body-parser');
const rabbitMQConfig = require('../config/rabbitmq');

const app = express();
app.use(bodyParser.json());

rabbitMQConfig.connectRabbitMQ((channel) => {

    channel.consume(rabbitMQConfig.ORDER_CHECKOUT_QUEUE, (msg) => {
        const order = JSON.parse(msg.content.toString());
        const paymentStatus = processPayment(order.amount);

        channel.publish(
            rabbitMQConfig.CHECKOUT_EXCHANGE,
            rabbitMQConfig.PAYMENT_STATUS_ROUTING_KEY,
            Buffer.from(JSON.stringify({ orderId: order.id, success: paymentStatus }))
        );

        channel.ack(msg);
        console.log(`Pagamento processado ${JSON.stringify(order.id)}`);
    });
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`API Gateway executando na porta ${PORT}`);
});

// SIMULAÇÃO DE PAGAMENTO, SE O VALOR FOR PAR APROVA SE NÃO CANCELA
function processPayment(amount) {
    if (amount == null)
        return false;

    var rest = Math.round(amount) % 2;
    return rest == 0;    
}