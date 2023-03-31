import {useEffect, useState} from 'react';
import KidsService from '../API/KidsService';
import Loader from "../UI/Loader/Loader";

function KidsWithRelatives({tokens, setTokens}) {
    const [kids, setKids] = useState([]);
    const [loader, setLoader] = useState(true);
    useEffect(() => {
        const fetchData = async () => {
            const data = await KidsService.getKidsWithRelatives(tokens, setTokens);
            setKids(data);
            setLoader(false);
        };
        fetchData();
    }, []);
    return (
        <div style={{alignItems: "center"}}>
            <h3>Kids with relatives</h3>
            {loader ? (
                <div
                    style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        height: "50vh",
                    }}
                >
                    <Loader/>
                </div>
            ) : kids.length > 0 ? (
                <table
                    style={{
                        width: "100%",
                        margin: "auto",
                        textAlign: "center",
                        tableLayout: "fixed",
                    }}
                >
                    <thead>
                    <tr>
                        <th style={{width: "20%"}}>Name</th>
                        <th style={{width: "20%"}}>Birth year</th>
                        <th style={{width: "60%"}}>Relatives</th>
                    </tr>
                    </thead>
                    <tbody>
                    {kids.map((kid) => (
                        <tr key={kid.id}>
                            <td style={{verticalAlign: "middle"}}>{kid.name}</td>
                            <td style={{verticalAlign: "middle"}}>{kid.birthYear}</td>
                            <td style={{verticalAlign: "middle"}}>
                                {kid.relatives.length > 0 ? (
                                    <table
                                        style={{
                                            width: "100%",
                                            margin: "0 auto", // обновленный стиль
                                            textAlign: "center",
                                            tableLayout: "fixed",
                                        }}
                                    >
                                        <thead>
                                        <tr>
                                            <th style={{width: "33%"}}>Name</th>
                                            <th style={{width: "33%"}}>Address</th>
                                            <th style={{width: "33%"}}>Phone</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {kid.relatives.map((relative) => (
                                            <tr key={relative.id}>
                                                <td style={{verticalAlign: "middle"}}>
                                                    {relative.name}
                                                </td>
                                                <td style={{verticalAlign: "middle"}}>
                                                    {relative.address}
                                                </td>
                                                <td style={{verticalAlign: "middle"}}>
                                                    {relative.phone}
                                                </td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <h3 style={{textAlign: "center"}}>No relatives found</h3>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <h3 style={{textAlign: "center"}}>No kids found</h3>
            )}
        </div>
    );
}

export default KidsWithRelatives;