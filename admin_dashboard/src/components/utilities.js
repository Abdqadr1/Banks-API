import { Spinner } from "react-bootstrap";
export function listFormData(data){
    if(!process.env.NODE_ENV || process.env.NODE_ENV === "development"){
        for (const pair of data.entries()) {
            console.log(pair[0] + ", " + pair[1]);
        }
    }
      
}
export const SPINNERS_BORDER_HTML = `<div class="spinner-border spinner-border-sm text-dark" role="status">
                                        <span class="visually-hidden">Loading...</span>
                                    </div>`
export const SPINNERS_BORDER = <Spinner animation="border" size="sm" className="d-block m-auto" style={{ width: "4rem", height: "4rem" }} />

export const useThrottle = (cb, delay) => {
    let shouldWait = false;
    let waitArgs = null;
    let setTimeoutFunc = () => {
        if (waitArgs == null) {
            shouldWait = false;
        }else {
            cb(...waitArgs);
            waitArgs = null;
            setTimeout(setTimeoutFunc, delay);
        }
    };

    return (...args) => {
      if(shouldWait) {
        waitArgs = args
        return;
      }
      cb(...args)
      shouldWait = true;
      setTimeout(setTimeoutFunc, delay)
    };
}
