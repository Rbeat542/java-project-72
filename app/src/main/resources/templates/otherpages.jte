@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@import gg.jte.Content
@param Content content
@param Content title
@param BasePage page = null

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</head>
<header>
    <div class="btn-group">
        <a href="${NamedRoutes.root()}" class="btn btn-primary">Main page</a>
        <a href="${NamedRoutes.urlsPath()}" class="btn btn-primary">URLs in Database</a>
        <a href="${NamedRoutes.build()}" class="btn btn-primary">Add URL</a>
    </div>
</header>
<body style="padding:10px">
<!-- Основная разметка страницы -->
<div class="content">
    ${content}
</div>
</body>
</html>