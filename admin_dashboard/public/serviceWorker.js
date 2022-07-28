const cacheName = "static";
const assets = [
    "/",
    "/index.html",
    "/static/js/bundle.js",
    "/manifest.json",
    "https://fonts.googleapis.com/css2?family=Inconsolata&family=Overpass&display=swap",
    "https://fonts.googleapis.com/icon?family=Material+Icons",
    "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined",
    "https://fonts.gstatic.com/s/materialiconsoutlined/v107/gok-H7zzDkdnRel8-DQ6KAXJ69wP1tGnf4ZGhUcel5euIg.woff2",
    "https://fonts.gstatic.com/s/materialicons/v135/flUhRq6tzZclQEJ-Vdg-IuiaDsNcIhQ8tQ.woff2",
    "/favicon.ico",
    "/logo512.png",
    "/logo192.png",
]
this.addEventListener("install", event => {
    event.waitUntil(
        caches.open(cacheName).then(cache => {
            console.log("caching static assets");
            cache.addAll(assets)
        })
    )
    
});

this.addEventListener("activate", e => {
    console.log("worker activated");
});

this.addEventListener("fetch", evt => {
    const url = evt.request.url;
    const method = evt.request.method;
    console.log("fetch url: ", evt);
    evt.respondWith(
        caches.match(evt.request)
            .then(cacheRes => {
                return cacheRes ?? fetch(evt.request);
            })
    )
})