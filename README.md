<img alt="logo" src="https://eo-cqrs.github.io/.github/eo-cqrs.svg" height="100px" />

[![Managed By Self XDSD](https://self-xdsd.com/b/mbself.svg)](https://self-xdsd.com/p/eo-cqrs/eo-kafka?provider=github)

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/eo-cars/eo-kafka)](https://www.rultor.com/p/eo-cqrs/xfake)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
<br>

[![mvn](https://github.com/eo-cqrs/xfake/actions/workflows/mvn.yaml/badge.svg)](https://github.com/eo-cqrs/xfake/actions/workflows/mvn.yaml)
[![maven central](http://maven-badges.herokuapp.com/maven-central/io.github.eo-cqrs/xfake/badge.svg)](https://search.maven.org/artifact/io.github.eo-cqrs/xfake)
[![javadoc](https://javadoc.io/badge2/io.github.eo-cqrs/xfake/javadoc.svg)](https://javadoc.io/doc/io.github.eo-cqrs/xfake)
[![codecov](https://codecov.io/gh/eo-cqrs/xfake/branch/master/graph/badge.svg?token=geuO10j79O)](https://codecov.io/gh/eo-cqrs/xfake)

[![Hits-of-Code](https://hitsofcode.com/github/eo-cqrs/xfake)](https://hitsofcode.com/view/github/eo-cqrs/xfake)
[![Lines-of-Code](https://tokei.rs/b1/github/eo-cqrs/xfake)](https://github.com/eo-cqrs/xfake)
[![PDD status](http://www.0pdd.com/svg?name=eo-cqrs/xfake)](http://www.0pdd.com/p?name=eo-cqrs/xfake)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/eo-cqrs/xfake/blob/master/LICENSE)

Project architect: [@h1alexbel](https://github.com/h1alexbel)

XML In-Memory Storage for your Fake Objects.

Read about [_Fake Objects pattern_](https://www.yegor256.com/2014/09/23/built-in-fake-objects.html) and Watch [_Why Mocking Frameworks are Evil_](https://www.youtube.com/watch?v=1bAixLaOCSA) by [@yegor256](https://github.com/yegor256).

**Motivation**. To create a fake objects, we need to introduce some place where data will be stored.
<br>
We're not happy with creating this one again and again.

**Principles**. These are the [design principles](https://www.elegantobjects.org/#principles) behind eo-kafka.

**How to use**. All you need is this (get the latest
version [here](https://search.maven.org/artifact/io.github.eo-cqrs/xfake)):

Maven:
```xml
<dependency>
  <groupId>io.github.eo-cqrs</groupId>
  <artifactId>xfake</artifactId>
</dependency>
```

Gradle:
```groovy
dependencies {
    compile 'io.github.eo-cqrs:xfake:<version>'
}
```

### Constructing Storage
To create an in-memory storage, we need to provide the name of the file to create and root XML node.  
```java
final FkStorage storage = new InFile("fake-test", "<fake/>");
storage.xml();
```

The output of `storage.xml()` will be:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<fake/>
```

### Applying Directives
```java
final FkStorage storage = new InFile("fake-test", "<fake/>");
    storage.apply(
      new Directives()
        .xpath("/fake")
        .addIf("servers")
    );
```

The result of the applying will be:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<fake>
  <servers/>
</fake>
```

This is the use-case of FkStorage in [**eo-kafka**](https://github.com/eo-cqrs/eo-kafka/blob/master/src/main/java/io/github/eocqrs/kafka/fake/InXml.java).
## How to Contribute

Fork repository, make changes, send us a [pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full Maven build:

```bash
$ mvn clean install
```

You will need Maven 3.8.7+ and Java 17+.

If you want to contribute to the next release version of eo-kafka, please check
the [project board](https://github.com/orgs/eo-cqrs/projects/2/views/1).

Our [rultor image](https://github.com/eo-cqrs/eo-kafka-rultor-image) for CI/CD.
