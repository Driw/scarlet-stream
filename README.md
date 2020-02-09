# Scarlet Stream

A library to read files that contains native values as raw bytes without compression, encryption or something like that.

For example to read correctly image data (ex: Bitmap - BMP) where has some int attributes as 4 bytes.

The library can read bytes into native types, String and Object and write native types, String and Object into bytes to be stored or sended some way.

We can read and write with byte buffers with byte array, from files with FileChannel or Sockets with InputStream and OutputStream.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need have [Maven](https://maven.apache.org/) to install Scarlet Stream and you can found it on [Maven Repository](https://mvnrepository.com/).

### Installing

Specify the maven import as the following example on your [pom.xml](https://maven.apache.org/pom.html) inside of your project.

```
<!-- https://mvnrepository.com/artifact/org.diverproject/scarlet-strem -->
<dependency>
    <groupId>org.diverproject</groupId>
    <artifactId>scarlet-strem</artifactId>
    <version>0.1.0</version>
</dependency>
```

1. Reading parsed data from array of bytes.

```java
byte[] bytes = new byte[] { ... } // Your byte array with data as bytes
DefaultBufferInput defaultBufferInput = new DefaultBufferInput()
	.setBufferReader(
		new DefaultBufferReader()
			.setByteBuffer(new DefaultByteBuffer(bytes))
	);
short shortValue = defaultBufferInput.getShort();
int intValue = defaultBufferInput.getInt();
long longValue = defaultBufferInput.getLong();
float floatValue = defaultBufferInput.getFloat();
double doubleValue = defaultBufferInput.getDouble();
```

2. Write values into array of bytes.

```java
byte[] data = new byte[...] // Your byte array to store the data
DefaultBufferOutput defaultBufferOutput = new DefaultBufferOutput()
	.setBufferWriter(
		new DefaultBufferWriter()
			.setByteBuffer(
				new DefaultByteBuffer(data)
			)
	);

defaultBufferOutput.put((short) 1);
defaultBufferOutput.put(1);
defaultBufferOutput.put(1L);
defaultBufferOutput.put(1F);
defaultBufferOutput.put(1D);
```

## Running the tests

The test can be execute just running the project with JUnit Test 5.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/diverproject/diamond-lang/tags).

*The revision log have somethings different because it's used developers found more easy changes made and make github commit messages more **clean***.

## Authors

* **Andrew Mello da Silva** - *Developer* - [Driw](https://github.com/Driw)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* **Billie Thompson** - *[Readme template](https://gist.github.com/PurpleBooth/109311bb0361f32d87a2)* - [PurpleBooth](https://github.com/PurpleBooth)
