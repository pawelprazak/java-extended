Release 1.0.4
=============
Added various bits and pieces, and more unit tests.

Code contained is quite mature, but API's can evolve over time (especially those marked @Beta).

New dependencies versions:

- Guava 19.0

Details:
 create core/src/main/java/com/bluecatcode/core/exceptions/CheckedException.java
 create core/src/main/java/com/bluecatcode/core/exceptions/UncheckedException.java
 create guava/src/main/java/com/bluecatcode/common/base/CheckedBlock.java
 create guava/src/main/java/com/bluecatcode/common/base/CheckedConsumer.java
 create guava/src/main/java/com/bluecatcode/common/base/CheckedEffect.java
 create guava/src/main/java/com/bluecatcode/common/concurrent/Futures.java
 create guava/src/main/java/com/bluecatcode/common/concurrent/Sleep.java
 create guava/src/main/java/com/bluecatcode/common/concurrent/Try.java
 create junit/src/main/java/com/bluecatcode/junit/rules/Timeouts.java

Release 1.0.3
=============
First production release with basic utilities.

Code contained is quite mature, but API's can evolve over time.

Dependencies versions:

- Guava 15.0, 16.0.1, 17.0, 18.0
- Joda Time 2.1, 2.3, 2.9.1
- Hamcrest 1.3
- Mockito 1.10.19
- JUnit 4.10, 4.11, 4.12