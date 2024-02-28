import { useState, useEffect } from "react";

function Countdown(props) {
    const { expirationDate } = props;
    const [remTime, setRemTime] = useState(new Date(expirationDate) - new Date());

    useEffect(() => {
        const interval = setInterval(() => {
            setRemTime((prev) => {
                const current = prev - 1000;

                if(current < 0) {
                    clearInterval(interval);
                }

                return current;
            });
        }, 1000);

        return () => clearInterval(interval);
    }, []);

    const formatRemTime = (remTime) => {
        if(remTime < 0) {
            return "Auction expired";
        }

        const time = Math.floor(remTime / 1000);
        const days = Math.floor(time / (60 * 60 * 24));
        const hours = Math.floor((time - (days * 60 * 60 * 24)) / (60 * 60));
        const minutes =  Math.floor((time - (days * 60 * 60 * 24) - (hours * 60 * 60)) / (60));
        const seconds = time % (60);

        return `${days >= 10 ? `${days}d` : `0${days}d`} 
                ${hours >= 10 ? hours : `0${hours}`}h  
                ${minutes >= 10 ? minutes : `0${minutes}`}m 
                ${seconds >= 10 ? seconds : `0${seconds}`}s`;
    }

    return (
        remTime >= 0 ? <p>Expires in: {formatRemTime(remTime)}</p> : "Auction expired"
    );
}

export default Countdown;