@import hexlet.code.model.UrlsPage
@import hexlet.code.repository.UrlRepository
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.otherpages(
page = page,
title = @`
    <title>Domains info</title>
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
    <p><a href="${NamedRoutes.build()}"> Add a domain</a></p>

    @if(UrlRepository.getEntities().isEmpty())
        <p>No urls added yet! </p>
    @else
        @for(var url : UrlRepository.getEntities())
            <p><a href="${NamedRoutes.urlPath(url.getId())}">Id:${url.getId()}</a>   Name:  ${url.getName()} ,   created at: ${url.getCreatedAt()}  </p>
        <br>
        @endfor
    @endif`
)