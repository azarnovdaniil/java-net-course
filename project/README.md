1. Не делать тяжёлых операций в pipeline. Если нужны сложные вычисления их лучше
   через executor service выносить в отдельный Thread и не занимать Thread worker group.