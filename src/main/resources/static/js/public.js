/**
 * Created by cellargalaxy on 18-4-11.
 */

function createListFileVue() {
    var listFileVue = new Vue({
            el: '#listFile',
            data: {
                files: []
            },
            methods: {
                restore: function (id, filename) {
                    try {
                        restore(id, filename);
                    } catch (err) {
                        alert('发生了一个异常:\n' + err);
                    }
                },
                backup: function (id, filename) {
                    try {
                        backup(id, filename);
                    }catch(err){alert('发生了一个异常:\n'+err);}
                }
                ,
                remove: function (id, filename) {
                    try {
                        remove(id, filename);
                    } catch (err) {
                        alert('发生了一个异常:\n' + err);
                    }
                }
                ,
                loadData: function (data) {
                    if (data.result) {
                        this.files = data.data;
                    }
                }
            }
        }
        )
    ;
    return listFileVue;
}
function createListLogVue() {
    var listLogVue = new Vue({
        el: '#listLog',
        data: {
            logs: []
        },
        methods: {
            loadData: function (data) {
                if (data.result) {
                    this.logs = data.data;
                }
            }
        }
    });
    return listLogVue;
}
function createPaginationVue(vue, getInfoUrl, getPagesUrl) {
    var paginationVue = new Vue({
        el: '#pagination',
        data: {
            pages: [], vue: vue, getInfoUrl: getInfoUrl, getPagesUrl: getPagesUrl
        },
        methods: {
            flipPage: function (page) {
                try {
                    getInfoByPage(page, this.getInfoUrl, vue);
                    getInfoByPage(page, this.getPagesUrl, this);
                } catch (err) {
                    alert('发生了一个异常:\n' + err);
                }
            },
            loadData: function (data) {
                if (data.result) {
                    this.pages = data.data;
                }
            }
        }
    });
    return paginationVue;
}