@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.allpages(
page = page,
title = @`
    Url analyzer. Index of urls
`,
content = @`

    @if(page != null && page.getFlashText() != null)
        @if(page.getFlashText() != null)
            <div class="alert alert-${page.getFlashType()}" role="alert">
                ${page.getFlashText()}
            </div>
        @endif
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
            !{var check = page.getLatestChecks().get(url.getId()); }
            <tr>
                <th>${url.getId()}</th>
                <th><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></th>
                @if(check != null)
                    <th>${check.getStatusCode()}</th>
                    <th>${check.getCreatedAt().toString()}</th>
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