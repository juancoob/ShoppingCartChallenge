# ShoppingCartChallenge 🛒
An Android app that lists dorms to book beds and price them into a shopping cart in some different currencies.

## How it is done? 👷

I have used a **Clean Architecture**. That means the project has some modules to separate their scope in that order:
- **app** is the Android module, and there are Android frameworks and data source implementations.
- **usecases** is a Kotlin module that stores use cases to be invoked from the Android module.
- **data** is a Kotlin module that stores repositories and datasources.
- **domain** is a Kotlin module that stores data classes to save the data the app is going to collect from the server.
- **testshared** is a Kotlin module that stores mocked local models for testing purposes

The presentation pattern used is **MVVM** to survive configuration changes.

## Reasoning 🤔

- I have used a clean architecture to have a separation of layers to have an scalable project.
- I have implemented MVVM as a design pattern because it is easier to follow the UDF.
- I have used Retrofit as an API REST client.
- I have managed the Arrow library to manage errors
- I have applied Hilt as DI and Room as DB because they are the recommended options by google.
- I have coded as clear as possible to be understandable by following `SOLID` principles. As _Robert C. Martin_ says, no code documentation is needed if the code is clear enough.
- I have created unit tests to test all the functionality.

## TO-DO 👨‍🔧
- Finish CheckoutViewModelTest with more cases.
- Add integration and UI tests to check the code quality.
- Start the bonus part
- Better gradle imports
