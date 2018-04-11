/**
 * Created by cellargalaxy on 18-1-10.
 */
var createFilePackagePagesUrl = null;
var findFilePackagesUrl = null;
var createTaskPagesUrl = null;
var findTasksUrl = null;
var synchronizeUrl = null;
var restoreFilePackageByIdUrl = null;
var backupFilePackageByIdUrl = null;
var removeFilePackageByIdUrl = null;
var backupAllFilePackageUrl = null;
var restoreAllFilePackageUrl = null;

function getCreateFilePackagePagesUrl() {
    if (createFilePackagePagesUrl == null || createFilePackagePagesUrl == '') {
        createFilePackagePagesUrl = $("#createFilePackagePages").attr("value");
    }
    return createFilePackagePagesUrl;
}
function getFindFilePackagesUrl() {
    if (findFilePackagesUrl == null || findFilePackagesUrl == '') {
        findFilePackagesUrl = $("#findFilePackages").attr("value");
    }
    return findFilePackagesUrl;
}
function getCreateTaskPagesUrl() {
    if (createTaskPagesUrl == null || createTaskPagesUrl == '') {
        createTaskPagesUrl = $("#createTaskPages").attr("value");
    }
    return createTaskPagesUrl;
}
function getFindTasksUrl() {
    if (findTasksUrl == null || findTasksUrl == '') {
        findTasksUrl = $("#findTasks").attr("value");
    }
    return findTasksUrl;
}
function getSynchronizeUrl() {
    if (synchronizeUrl == null || synchronizeUrl == '') {
        synchronizeUrl = $("#synchronize").attr("value");
    }
    return synchronizeUrl;
}
function getRestoreFilePackageByIdUrl() {
    if (restoreFilePackageByIdUrl == null || restoreFilePackageByIdUrl == '') {
        restoreFilePackageByIdUrl = $("#restoreFilePackageById").attr("value");
    }
    return restoreFilePackageByIdUrl;
}
function getBackupFilePackageByIdUrl() {
    if (backupFilePackageByIdUrl == null || backupFilePackageByIdUrl == '') {
        backupFilePackageByIdUrl = $("#backupFilePackageById").attr("value");
    }
    return backupFilePackageByIdUrl;
}
function getRemoveFilePackageByIdUrl() {
    if (removeFilePackageByIdUrl == null || removeFilePackageByIdUrl == '') {
        removeFilePackageByIdUrl = $("#removeFilePackageById").attr("value");
    }
    return removeFilePackageByIdUrl;
}
function getBackupAllFilePackageUrl() {
    if (backupAllFilePackageUrl == null || backupAllFilePackageUrl == '') {
        backupAllFilePackageUrl = $("#backupAllFilePackage").attr("value");
    }
    return backupAllFilePackageUrl;
}
function getRestoreAllFilePackageUrl() {
    if (restoreAllFilePackageUrl == null || restoreAllFilePackageUrl == '') {
        restoreAllFilePackageUrl = $("#restoreAllFilePackage").attr("value");
    }
    return restoreAllFilePackageUrl;
}
////////////////////////////////////////////////////////////////////////////

function getInfoByPage(page, url, vue) {
    $.ajax({
        url: url,
        type: 'get',
        data: {page: page},
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",

        error: function () {
            alert("网络错误!");
        },
        success: function (data) {
            vue.loadData(data);
        }
    });
}

function uploadFile() {
    var addFileFormFile = $('#addFileFormFile');
    var addFileFormPathDate = $("#addFileFormPathDate");
    var addFileFormDescription = $("#addFileFormDescription");

    var files = addFileFormFile.prop('files');
    var date = addFileFormPathDate.val();
    var description = addFileFormDescription.val();
    if (files == null || files == '' || files.length == 0 || files[0] == null || files[0] == '') {
        alert('请选择文件');
        return;
    }
    if (description != null && description == '') {
        description = null;
    }
    var data = new FormData();
    data.append('file', files[0]);
    data.append('pathDate', date);
    data.append('description', description);
    $.ajax({
        url: $("#addFileForm").attr("action"),
        type: 'post',
        data: data,
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        cache: false,
        processData: false,
        contentType: false,

        error: function () {
            alert('网络异常');
        },
        success: function (data) {
            if (data.result) {
                alert('添加成功:' + data.data);
                location.reload();
            } else {
                alert('添加失败:' + data.data);
            }
        }
    });
}
function uploadHttpUrl() {
    var addHttpUrlFormUrl = $('#addHttpUrlFormUrl');
    var addHttpUrlFormPathDate = $("#addHttpUrlFormPathDate");
    var addHttpUrlFormDescription = $("#addHttpUrlFormDescription");

    var httpUrl = addHttpUrlFormUrl.val();
    var pathDate = addHttpUrlFormPathDate.val();
    var description = addHttpUrlFormDescription.val();
    $.ajax({
        url: $("#addHttpUrlForm").attr("action"),
        type: 'post',
        data: {httpUrl: httpUrl, pathDate: pathDate, description: description},
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",

        error: function () {
            alert('网络异常');
        },
        success: function (data) {
            if (data.result) {
                alert('添加成功:' + data.data);
                location.reload();
            } else {
                alert('添加失败:' + data.data);
            }
        }
    });
}

function backup(id, filename) {
    operateFile(id, filename, "确认备份文件?: ", getRestoreFilePackageByIdUrl());
}
function restore(id, filename) {
    operateFile(id, filename, "确认恢复文件?: ", getBackupFilePackageByIdUrl());
}
function remove(id, filename) {
    operateFile(id, filename, "确认删除文件?: ", getRemoveFilePackageByIdUrl());
}

function synchronize() {
    $.ajax({
        url: getSynchronizeUrl(),
        type: 'post',
        data: {},
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",

        error: function () {
            alert("网络错误!");
        },
        success: function (data) {
            if (data.result) {
                alert(data.data);
                location.reload();
            } else {
                alert(data.data);
            }
        }
    });
}

function backupAll() {
    operateAllFile("确认备份全部文件?", getBackupAllFilePackageUrl());
}
function restoreAll() {
    operateAllFile("确认恢复全部文件?", getRestoreAllFilePackageUrl());
}

function operateFile(id, filename, warm, url) {
    if (id == null || id == "") {
        alert("无效id: " + id);
        return;
    }
    if (filename == null || filename == "") {
        alert("无效文件名字: " + filename);
        return;
    }
    if (confirm(warm + filename + "(" + id + ")")) {
        $.ajax({
            url: url,
            type: 'post',
            data: {id: id},
            contentType: "application/x-www-form-urlencoded",
            dataType: "json",

            error: function () {
                alert("网络错误!");
            },
            success: function (data) {
                if (data.result) {
                    alert(data.data);
                    location.reload();
                } else {
                    alert(data.data);
                }
            }
        })
    }
}
function operateAllFile(warm, url) {
    if (confirm(warm)) {
        $.ajax({
            url: url,
            type: 'post',
            data: {},
            contentType: "application/x-www-form-urlencoded",
            dataType: "json",

            error: function () {
                alert("网络错误!");
            },
            success: function (data) {
                if (data.result) {
                    alert(data.data);
                    location.reload();
                } else {
                    alert(data.data);
                }
            }
        })
    }
}