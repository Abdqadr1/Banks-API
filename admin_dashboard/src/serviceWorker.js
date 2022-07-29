const cacheNames = ["static-assets-v1.1", "dynamic-assets-v1.0"];
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
    "/logo144.png",
    "/site-logo.ico"
];

this.addEventListener("install", event => {
    event.waitUntil(
        caches.open(cacheNames[0]).then(cache => {
            // console.log("caching static assets");
            cache.addAll(assets)
        })
    )
    
});

this.addEventListener("activate", e => {
    e.waitUntil(
        caches.keys().then(keys => {
            return Promise.all(
                keys.filter(key => cacheNames.some(k => k !== key))
                    .map(k => caches.delete(k))
            );
        })
    )
});

this.addEventListener("fetch", evt => {
    evt.respondWith(
        caches.match(evt.request)
            .then(cacheRes => {
                return cacheRes ?? fetch(evt.request);
                //     .then(fetchRes => {
                //     return caches.open(cacheNames[1]).then(cache => {
                //         // const url = evt.request.url;
                //         // if (dynamicAssets.some(s => url.endsWith(s))) { 
                //         //     // console.log(url);
                //         //     cache.put(url, fetchRes.clone());
                //         // } 
                //         return fetchRes;
                //     })
                // });
            })
    )
})