@import hexlet.code.model.UrlsPage
@import hexlet.code.util.NamedRoutes
@import hexlet.code.repository.UrlRepository
@import hexlet.code.repository.UrlCheckRepository
@param UrlsPage page

@template.otherpages(
page = page,
title = @`
    Web pages analyzer. Analyzed urls
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

    @if(UrlRepository.getEntities().isEmpty())
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
        @for(var url : UrlRepository.getEntities())
            <tr>
                <th>${url.getId()}</th>
                <th><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></th>
                @if(!UrlCheckRepository.getEntities(url.getId()).isEmpty())
                    !{var list = UrlCheckRepository.getEntities(url.getId());}
                    <th>${list.getLast().getCreatedAt()}</th>
                    <th>${list.getLast().getStatusCode()}</th>
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