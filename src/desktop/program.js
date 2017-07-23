const electron = require('electron');
const app = electron.app;
const BrowserWindow = electron.BrowserWindow;
const path = require('path');
const url = require('url');
var MainWindow;
var developer = false;
function event_create()
{
    MainWindow = new BrowserWindow(
        {
            width: 1000,
            height: 800,
            transparent: false,
            frame: true,
            show: false,
            title: "SBHS Maps",
        });
    MainWindow.loadURL(url.format(
        {
            pathname: path.join('index.html'),
            protocol: 'file',
            slashes: true
        }));
    MainWindow.on('closed', event_destroy);
    MainWindow.on('ready-to-show', event_load);
}
function event_load()
{
    if (developer === true)
    {
        MainWindow.openDevTools(
            {
                detached: true
            })
    }
    MainWindow.show();
}
function event_activate()
{
    if (MainWindow === null)
    {
        event_create();
    }
}
function event_close()
{
    if (process.platform !== 'darwin')
    {
        app.quit();
    }
}
function event_destroy()
{
    MainWindow = null;
}
function main()
{
    app.on('ready', event_create);
    app.on('window-all-closed', event_close);
    app.on('activate', event_activate);
}
main();