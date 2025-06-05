@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.allpages(
page = page,
title = @`
    Url analyzer. Index of urls
`,
content = @`

    @if(page != null && page.getFlash() != null && page.getFlash().equals("Страница успешно добавлена"))
        <div class="alert alert-success" role="alert">
            ${page.getFlash()}
        </div>
    @endif
    @if(page != null && page.getFlash() != null && page.getFlash().equals("Страница уже существует"))
        <div class="alert alert-warning" role="alert">
            ${page.getFlash()}
        </div>
    @endif

    @if(page.getUrls().isEmpty())
        <br>
        <p>No urls added yet!</p>
    @else
        <table class="table">
            <caption>
                Urls
            </caption>
            <thead>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">Name</th>
                <th scope="col">Последняя проверка</th>
                <th scope="col">Код ответа</th>
            </tr>
            </thead>
            <tbody>
        @for(var url : page.getUrls())
            !{var check = page.getLastStatuses().get(url.getId()); }
            <tr>
                <th>${url.getId()}</th>
                <th><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></th>
                @if(check != null)
                    <th>${check.getStatus()}</th>
                    <th>${check.getCheckTime().toString()}</th>
                @else
                    <th></th>
                    <th></th>
                @endif
            </tr>
        @endfor
            </tbody>
        </table>
    @endif`
)