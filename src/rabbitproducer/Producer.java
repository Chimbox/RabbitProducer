package rabbitproducer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dominio.Operacion;
import dominio.Operador;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Alfonso Felix
 */
public class Producer {

    private static final String QUEUE_NAME = "mensajes";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");

        try {

            Connection connection = (Connection) factory.newConnection();

            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            Operacion operacion = new Operacion();

            Random x = new Random();

            while (true) {

                switch (x.nextInt(5)) {
                    case 1:
                        operacion = new Operacion(x.nextInt(1000), x.nextInt(1000), Operador.SUMA);
                        break;
                    case 2:
                        operacion = new Operacion(x.nextInt(1000), x.nextInt(1000), Operador.RESTA);
                        break;
                    case 3:
                        operacion = new Operacion(x.nextInt(1000), x.nextInt(1000), Operador.MULTIPLICACION);
                        break;
                    case 4:
                        operacion = new Operacion(x.nextInt(1000), x.nextInt(1000), Operador.DIVISION);
                        break;
                }

                channel.basicPublish("", QUEUE_NAME, null, convertToBytes(operacion));

                System.out.println("Objeto enviado con operador: "+operacion.getOperador());

                Thread.sleep(1000);

            } 
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }
}
