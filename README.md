# Android-messaging-App
Local instant messaging Android App

Funcion del servidor

1- Crea un objeto socket para escuchar conexiones
2- Cuando recibe conexion envia mensaje global a todos los conectados para notificar
3- Se mantiene esperando datos de esta conexion abierta
4- Cuando recibe datos los interpreta y responde como corresponda

Funcion del cliente

1- Conecta a una IP puerto por via Socket
2- Cuando conecta envia un paquete con su nombre
3- Recibe la lista de clientes logeados
4- Cuando envia un mensaje; manda un formato especifico


los formatos son

Conectado =>

+login|NOMBRE USUARIO

Desconectado =>

+logout|NOMBRE USUARIO

Mensaje=>

+msg|NOMBRE USUARIO|MENSAJE

Lista de usuarios =>

+lst|NOMBRE USUARIO;NOMBRE USUARIO;NOMBRE USUARIO...

------------

Adaptadores se utilizan en el listado de mensajes
Event Listener en los botones de conectar,crear, y enviar mensaje
Views en la pantalla de Usuarios Conectados

