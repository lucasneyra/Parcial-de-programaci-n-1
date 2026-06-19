Parcial 2 de Programacion III - EcoRide PRO

GRUPO: Bots

Integrantes:

Alberto Morales Aveiro
Lucas Ismael Neyra Molina
Martin Ignacio Caniza
Peredo Yucra Jose




1) Conclusion EcoRide PRO
El trabajo permitio cambiar el sistema EcoRide desde una implementacion funcional pero fragil hacia una arquitectura mejorada, escalable y mantenible A continuacion se sintetizan los aprendizajes concretos obtenidos en cada uno de los entregables requeridos

Diagrama de clases:
El diagrama muestrra con precision los tres grandes cambios estructurales introducidos en esta version En primer lugar, el ciclo de vida del vehiculo dejo de ser un atributo booleano o un campo de texto para convertirse en un conjunto de clases de estado EstadoEnEspera EstadoEnViaje EstadoEnReparacion que implementan la interfaz EstadoVehiculo Cada estado conoce exactamente que transiciones permite y cuales rechaza eliminando por completo los bloques if else o switch en el modelo Patron State En segundo lugar el calculo dinamico de tarifas quedo representado mediante la interfaz CriterioTarifa y sus tres implementaciones concretas desacopladas del servicio mediante la fabrica FabricaCriterio Patron Strategy En tercer lugar el ordenamiento aparece claramente separado en dos mecanismos distintos el Comparable intrinseco de vehiculo criterio natural por bateria y el ComparadorPorCostoBase externo criterio alternativo por tarifa evidenciando que ambos pueden coexistir sin interferirse

El diagrmma de clases primer parcil.jpg, pero lo modificamos a uno nuevo como diagrama segundo parcial.jpg

Codigo Fuente - Arquitectura Linpia en Capas:
El repositorio respeta estrictamente la separacion de responsabilidades exigida por Spring Boot La capa model concentra todas las entidades patrones de diseño interfaz de pago y mecanismos de comparacion La capa servicio coordina la logica de negocio sin depender de detalles de infraestructura La capa controllers expone unicamente DTOs al exterior protegiendo las entidades internas de filtrarse hacia la red


Se presto especial atencion a dos aspectos criticos Primero la estructura de datos EstacionAnclaje migro de una List a un hashmap para garantizar que la busqueda por patente sea inmediata, sin importar el tamaño de la flota Segundo las interfaces de comparacion vehiculo implementa Comparable para su ordenamiento natural y ComparadorPorCostoBase implementa Comparator como criterio externo siguiendo correctamente el contrato del lenguaje Java sin violar el principio de responsabilidad unica

2) Anexo Tecnico de Rendimiento


Para cumplir con los requerimientos avanzados de la catedra respecto a la optimizacion de algoritmos y el diseño desacoplado de la aplicacion se implementaron las siguientes soluciones:

Busqueda Directa O(1) de Vehiculos vs Busqueda Lineal O(N):
En la version inicial para buscar un vehiculo por su patente dentro de las estaciones se recorrian todas las estaciones y luego se iteraba sobre las listas de vehiculos elemento por elemento (busqueda lineal) Si el sistema crece a 100000 vehiculos esto resulta muy ineficiente porque en el peor de los casos hay que realizar N comparaciones de cadenas de texto
Para solucionar esto en la clase alquilerService incorporamos un mapa de patentes global (flota) usando un HashMap La busqueda mediante flota get(patente) utiliza una tabla Hash interna de java lo cual permite localizar cualquier rodado en tiempo constante O(1) sin importar cuantos vehiculos esten registrados Ademas cada vehiculo mantiene una referencia directa a su EstacionAnclaje actual por lo que podemos desanclarlo en tiempo constante sin necesidad de recorrer la coleccion de estaciones

Algoritmo de Deduplicacion de GPS en un solo paso O(N) vs Bucles Anidados O(N2):
Si se usaran dos bucles anidados para comparar cada coordenada recibida contra todas las demas de la lista el algoritmo tendria una complejidad de O(N2) Con miles de coordenadas este procesamiento saturaria el CPU y congelaria el servidor
La solucion consiste en insertar todos los elementos de la lista en una estructura de tipo LinkedHashSet Al recorrer la lista original de reportes una sola vez intentamos agregar cada coordenada al set Debido a la propiedad de los sets (que no permiten elementos duplicados) y a que sobreescribimos los metodos equals y hashCode en la clase ReporteGPS el set filtra de forma automatica los duplicados en tiempo constante O(1) por insercion De esta forma el algoritmo corre en tiempo lineal O(N) realizando una unica pasada por los reportes optimizando drasticamente el uso de CPU y memoria

Ordenamiento Natural (Comparable) y Alternativo (Comparator):
Para permitir que el sistema muestre la flota bajo dos criterios de ordenamiento diferentes sin colisionar en memoria aplicamos dos estrategias complementarias de Java:

#ordeenamiento natural (Prioridad de Carga): Hicimos que la clase Vehiculo implemente la interfaz Comparable y sobreescribimos el metodo compareTo comparando los niveles de bateria de menor a mayor Esto establece la prioridad intrinseca por defecto de la flota cuando se llama a Collections sort(lista)



#Ordenamiento aalternativo (Tarifa Descendente): Creamos una clase independiente llamada ComparadorPorCostoBase que implementa la interfaz Comparator comparando las tarifas base de mayor a menor Al solicitar este orden comercial secundario pasamos este comparador como segundo parametro a Collections sort(lista new ComparadorPorCostoBase)
Esto nos permite ordenar las colecciones en memoria de manera dinamica y desacoplada sin alterar las clases de dominio ni romper el criterio por defecto Ademas para cumplir estrictamente con las directivas del parcial no se utilizaron expresiones lanbda ni la API de Streams resolviendo todo mediante bucles y clases tradicionales


3) Reflexion Final
Este examen demostro que el diseño orientado a objetos no es un fin en si mismo sino un medio para construir sistemas que puedan crecer sin romperse El State Pattern permite agregar nuevas fases al ciclo de vida del vehiculo creando una clase nueva sin tocar las existentes El Strategy Pattern permite incorporar nuevos criterios de tarifa sin modificar el servicio El uso de estructuras de datos adecuadas (hashMap linkedHashSet) convierte operaciones que antes eran cuellos de botella en operaciones de tiempo constante Y la correcta declaracion de Comparable y Comparator permite ofrecer multiples vistas ordenadas de los mismos datos sin duplicarlos ni modificar el modelo base En conjunto estas decisiones son las que diferencian un codigo que funciona de un codigo que escala.
