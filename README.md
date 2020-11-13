# BWF
BlockWit Web Framework
## Installation and Getting Started
Установка зависимостей:  
`mvn install`  

Запуск проекта в режиме разработки:  
`mvn spring-boot:run`

IDE при первом запуске может предложить выполнить `npm install`.
Этого делать не надо. Зависимости для фронта подтягиваются через Maven.  
В случае, если все-таки `npm install` был выполнен из командной строки и сборка проекта крашится чем-то вроде `Node Sass could not find a binding for your current environment`, удалите папку `node_modules` и смело запускайте проект командой `mvn spring-boot:run`
