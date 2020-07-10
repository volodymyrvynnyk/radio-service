<#include "../include/header.ftl">

<div class="container">
    <div class="py-3">
        <a href="/admin/videos/add">
            <div class="btn btn-primary btn-lg btn-block">Додати відео</div>
        </a>
    </div>
    <#list videos as video>
        <div class="row w-80 my-3 p-4 bg-light">
            <div class="col">
                <iframe
                        src="https://www.youtube.com/embed/${video.id}">
                </iframe>
            </div>

            <div class="col-8">
                <div class="row">
                    <strong>${video.title}</strong>
                </div>

                <form action="/admin/videos/updatePriority/${video.id}" method="post">
                    <div class="form-row form-inline">
                        <span class="my-3 ml-3">Приорітет</span>
                        <select class="form-control inline mx-3" name="newPriority" onchange="this.form.submit()"
                                id="prioritySelect">
                            <#list 1..videos?size as number>
                                <option value="${number}" <#if number == (video?index + 1)>selected</#if>>
                                    ${number}
                                </option>
                            </#list>
                        </select>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
                <div class="row float-right">
                    <a href="/admin/videos/delete/${video.id}">
                        <div class="btn btn-danger row mx-4">Видалити</div>
                    </a>
                </div>
            </div>
        </div>
    </#list>
</div>

<#include "../include/toastr.ftl">
<#include "../include/footer.ftl">