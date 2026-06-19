# Parcial de Programación III — EcoRide PRO

**GRUPO:** Bots

**Integrantes:**
- Alberto Morales Aveiro
- Lucas Ismael Neyra Molina
- Martin Ignacio Caniza
- Peredo Yucra Jose

---

# Conclusión — EcoRide PRO
## Programación III · Examen Parcial Práctico II (Avanzado) · 2026

El presente trabajo permitió evolucionar el sistema EcoRide desde una implementación funcional pero frágil hacia una arquitectura profesional, escalable y mantenible. A continuación se sintetizan los aprendizajes concretos obtenidos en cada uno de los entregables requeridos.

---

### 1. Diagrama de Clases UML Avanzado

El diagrama refleja con precisión los tres grandes cambios estructurales introducidos en esta versión. En primer lugar, el **ciclo de vida del vehículo** dejó de ser un atributo booleano o un campo de texto para convertirse en un conjunto de clases de estado (`EstadoEnEspera`, `EstadoEnViaje`, `EstadoEnReparacion`) que implementan la interfaz `EstadoVehiculo`. Cada estado conoce exactamente qué transiciones permite y cuáles rechaza, eliminando por completo los bloques `if/else` o `switch` en el modelo. En segundo lugar, el **cálculo dinámico de tarifas** quedó representado mediante la interfaz `CriterioTarifa` y sus tres implementaciones concretas, desacopladas del servicio mediante la fábrica `FabricaCriterio`. En tercer lugar, el **ordenamiento** aparece claramente separado en dos mecanismos distintos: el `Comparable` intrínseco de `Vehiculo` (criterio natural por batería) y el `ComparadorPorCostoBase` externo (criterio alternativo por tarifa), evidenciando que ambos pueden coexistir sin interferirse.

El diagrama UML se encuentra adjunto en la raíz como [diagrama de clases.jpeg](diagrama%20de%20clases.jpeg).

---

### 2. Código Fuente — Arquitectura Limpia en Capas

El repositorio respeta estrictamente la separación de responsabilidades exigida por Spring Boot. La capa `model` concentra todas las entidades, patrones de diseño, interfaces de pago y mecanismos de comparación. La capa `service` coordina la lógica de negocio sin depender de detalles de infraestructura. La capa `controllers` expone únicamente DTOs al exterior, protegiendo las entidades internas de filtrarse hacia la red.

Se prestó especial atención a dos aspectos críticos. Primero, la estructura de datos: `EstacionAnclaje` migró de una `List<Vehiculo>` a un `HashMap<String, Vehiculo>`, garantizando que la búsqueda por patente sea O(1) sin importar el tamaño de la flota. Segundo, las interfaces de comparación: `Vehiculo` implementa `Comparable<Vehiculo>` para su ordenamiento natural y `ComparadorPorCostoBase` implementa `Comparator<Vehiculo>` como criterio externo, siguiendo correctamente el contrato del lenguaje Java sin violar el Principio de Responsabilidad Única.

---

### 3. Anexo Técnico de Rendimiento (PERFORMANCE.md)

El documento técnico adjunto en la raíz del proyecto responde con precisión a las tres preguntas planteadas por la cátedra.

Sobre la **búsqueda de vehículos**: la versión anterior recorría una lista completa elemento por elemento, produciendo un tiempo O(n) que se degradaba linealmente con el crecimiento de la flota. La nueva implementación con `HashMap` calcula un hash de la patente y accede directamente a la posición de memoria correspondiente, logrando tiempo O(1) constante independientemente de si hay 10 o 100.000 vehículos registrados.

Sobre la **deduplicación de alertas GPS**: el algoritmo ingenuo compara cada coordenada entrante contra todas las ya procesadas, generando O(n²) operaciones que saturan la CPU ante volúmenes reales. La solución implementada usa `LinkedHashSet`, que detecta duplicados en O(1) por inserción gracias a su tabla hash interna, completando el proceso en una única pasada O(n) y preservando además el orden de llegada de los datos.

Sobre el **ordenamiento de flota**: se demostró que `Comparable` y `Comparator` no son excluyentes sino complementarios. Implementar `compareTo` en `Vehiculo` define el orden natural de la clase sin impedir que `Collections.sort` reciba en cualquier momento un `Comparator` externo que aplique un criterio completamente distinto. Ambos ordenamientos pueden ejecutarse en memoria de forma independiente y simultánea sin modificar ninguna clase existente.

---

### Reflexión Final

Este examen demostró que el diseño orientado a objetos no es un fin en sí mismo, sino un medio para construir sistemas que puedan crecer sin romperse. El **State Pattern** permite agregar nuevas fases al ciclo de vida del vehículo creando una clase nueva, sin tocar las existentes. El **Strategy Pattern** permite incorporar nuevos criterios de tarifa sin modificar el servicio. El uso de estructuras de datos adecuadas (`HashMap`, `LinkedHashSet`) convierte operaciones que antes eran cuellos de botella en operaciones de tiempo constante. Y la correcta declaración de `Comparable` y `Comparator` permite ofrecer múltiples vistas ordenadas de los mismos datos sin duplicarlos ni modificar el modelo base. En conjunto, estas decisiones son las que diferencian un código que funciona de un código que escala.
