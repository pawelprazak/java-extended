Release 1.1.0
=============
Module `core` was merged with `guava`.

Added `Either` and `contract/*` to `common`.
Also `Rules` and `AssumeMore` to `junit`,
and `CharSequenceSliceMatcher` and `MatcherBuilder` to `hamcrest`

Details:

    create mode 100644 guava/src/main/java/com/bluecatcode/common/base/CheckedBiFunction.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/base/CheckedFunction.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/base/Either.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/base/Exceptions.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/base/Left.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/base/Right.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/collections/package-info.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/contract/Checks.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/contract/Impossibles.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/contract/errors/ContractViolation.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/contract/errors/EnsureViolation.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/contract/errors/ImpossibleViolation.java
    create mode 100644 guava/src/main/java/com/bluecatcode/common/contract/errors/RequireViolation.java
    create mode 100644 hamcrest/src/main/java/com/bluecatcode/hamcrest/matchers/CharSequenceSliceMatcher.java
    create mode 100644 hamcrest/src/main/java/com/bluecatcode/hamcrest/matchers/MatcherBuilder.java
    create mode 100644 junit/src/main/java/com/bluecatcode/junit/rules/AssumeMore.java
    create mode 100644 junit/src/main/java/com/bluecatcode/junit/rules/Rules.java

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