# Parcial de Programación III - EcoRide Pro

**GRUPO:** Bots

**Integrantes:**
- Alberto Morales Aveiro
- Lucas Ismael Neyra Molina
- Martin Ignacio Caniza
- Peredo Yucra Jose

---

## Anexo Técnico de Rendimiento

Para cumplir con los requerimientos avanzados de la cátedra respecto a la optimización de algoritmos y el diseño desacoplado de la aplicación, implementamos y documentamos las siguientes soluciones:

### 1. Búsqueda Directa O(1) de Vehículos vs. Búsqueda Lineal O(N)
En la versión inicial, para buscar un vehículo por su patente dentro de las estaciones, recorríamos todas las estaciones y luego iterábamos sobre las listas de vehículos elemento por elemento (búsqueda lineal). Si el sistema crece a 100,000 vehículos, esto resulta muy ineficiente porque en el peor de los casos hay que realizar $N$ comparaciones de Strings.
Para solucionar esto, en la clase `alquilerService` incorporamos un mapa de patentes global (`Map<String, Vehiculo> flota = new HashMap<>()`). La búsqueda mediante `flota.get(patente)` utiliza una tabla Hash interna de Java, lo cual permite localizar cualquier rodado en tiempo constante **$O(1)$**, sin importar cuántos vehículos estén registrados. Además, cada vehículo mantiene una referencia directa a su `EstacionAnclaje` actual, por lo que podemos desanclarlo en tiempo constante sin necesidad de recorrer la colección de estaciones.

### 2. Algoritmo de Deduplicación de GPS en un solo paso O(N) vs. Bucles Anidados O(N²)
Si usáramos dos bucles anidados para comparar cada coordenada recibida contra todas las demás de la lista, el algoritmo tendría una complejidad de **$O(N^2)$**. Con miles de coordenadas, este procesamiento saturaría el CPU y congelaría el servidor.
Nuestra solución consiste en insertar todos los elementos de la lista en una estructura de tipo `LinkedHashSet`. Al recorrer la lista original de reportes una sola vez, intentamos agregar cada coordenada al set. Debido a la propiedad de los sets (que no permiten elementos duplicados) y a que sobreescribimos los métodos `equals` y `hashCode` en la clase `ReporteGPS`, el set filtra de forma automática los duplicados en tiempo constante $O(1)$ por inserción. De esta forma, el algoritmo corre en tiempo lineal **$O(N)$** realizando una única pasada por los reportes, optimizando drásticamente el uso de CPU y memoria.

### 3. Ordenamiento Natural (Comparable) y Alternativo (Comparator)
Para permitir que el sistema muestre la flota bajo dos criterios de ordenamiento diferentes sin colisionar en memoria, aplicamos dos estrategias complementarias de Java:
- **Ordenamiento Natural (Prioridad de Carga):** Hicimos que la clase `Vehiculo` implemente la interfaz `Comparable<Vehiculo>` y sobreescribimos el método `compareTo` comparando los niveles de batería de menor a mayor. Esto establece la prioridad intrínseca por defecto de la flota cuando se llama a `Collections.sort(lista)`.
- **Ordenamiento Alternativo (Tarifa Descendente):** Creamos una clase independiente llamada `ComparadorTarifaDescendente` que implementa la interfaz `Comparator<Vehiculo>` comparando las tarifas base de mayor a menor. Al solicitar este orden comercial secundario, pasamos este comparador como segundo parámetro a `Collections.sort(lista, new ComparadorTarifaDescendente())`.
Esto nos permite ordenar las colecciones en memoria de manera dinámica y desacoplada sin alterar las clases de dominio ni romper el criterio por defecto. Además, para cumplir estrictamente con las directivas del parcial, **no utilizamos expresiones lambda ni la API de Streams**, resolviendo todo mediante loops y clases tradicionales.
