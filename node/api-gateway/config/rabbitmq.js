const amqp = require('amqplib/callback_api');

const RABBITMQ_URL = 'amqp://host.docker.internal:5672'; 
//const RABBITMQ_URL = 'amqp://localhost';

const ORDER_CHECKOUT_QUEUE = 'order_checkout_queue';
const PAYMENT_STATUS_QUEUE = 'payment_status_queue';
const CHECKOUT_EXCHANGE = 'checkout_exchange';
const CHECKOUT_ROUTING_KEY = 'checkout_routing_key';
const PAYMENT_STATUS_ROUTING_KEY = 'payment_status_routing_key';

function connectRabbitMQ(callback) {
    amqp.connect(RABBITMQ_URL, (connectionError, connection) => {
        if (connectionError)
            throw connectionError;

        connection.createChannel((channelError, channel) => {
            if (channelError)
                throw channelError;

            channel.assertExchange(CHECKOUT_EXCHANGE, 'direct', { durable: true });
            channel.assertQueue(ORDER_CHECKOUT_QUEUE, { durable: true });
            channel.assertQueue(PAYMENT_STATUS_QUEUE, { durable: true });

            channel.bindQueue(ORDER_CHECKOUT_QUEUE, CHECKOUT_EXCHANGE, CHECKOUT_ROUTING_KEY);
            channel.bindQueue(PAYMENT_STATUS_QUEUE, CHECKOUT_EXCHANGE, PAYMENT_STATUS_ROUTING_KEY);

            console.log('RabbitMQ está configurado corretamente.');
            callback(channel);
        });
    });
}

module.exports = {
    connectRabbitMQ,
    CHECKOUT_EXCHANGE,
    ORDER_CHECKOUT_QUEUE,
    PAYMENT_STATUS_QUEUE,
    CHECKOUT_ROUTING_KEY,
    PAYMENT_STATUS_ROUTING_KEY
};
