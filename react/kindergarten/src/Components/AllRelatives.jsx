import {useEffect, useState} from "react";
import AllRelativesService from "../API/AllRelativesService";
import Loader from "../UI/Loader/Loader";
import {Dropdown} from "react-bootstrap";

function AllRelatives({tokens, setTokens}) {
    const [relatives, setRelatives] = useState([]);
    const [loader, setLoader] = useState(true);
    const [sortBy, setSortBy] = useState("default");

    const handleSortChange = (event) => {
        setSortBy(event);
    };

    useEffect(() => {
        const fetchData = async () => {
            let data;
            switch (sortBy) {
                case "name":
                    data = await AllRelativesService.getAllSortedByName(tokens, setTokens);
                    break;
                case "kidCount":
                    data = await AllRelativesService.getAllSortedByKidCount(tokens, setTokens);
                    break;
                case "address":
                    data = await AllRelativesService.getAllSortedByAddress(tokens, setTokens);
                    break;
                case "default":
                default:
                    data = await AllRelativesService.getAll(tokens, setTokens);
                    break;
            }
            setRelatives(data);
            setLoader(false);
        };
        fetchData();
    }, [sortBy, tokens, setTokens]);

    return (
        <div className="d-flex flex-column align-items-center">
            <h3>All relatives</h3>
            <div className="mb-3">
                <Dropdown onSelect={handleSortChange}>
                    <Dropdown.Toggle variant="secondary" id="sort-by">
                        Sort by: {sortBy}
                    </Dropdown.Toggle>

                    <Dropdown.Menu>
                        <Dropdown.Item eventKey="default">Default</Dropdown.Item>
                        <Dropdown.Item eventKey="name">Name</Dropdown.Item>
                        <Dropdown.Item eventKey="kidCount">Number of Kids</Dropdown.Item>
                        <Dropdown.Item eventKey="address">Address</Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            </div>

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
            ) : relatives.length > 0 ? (
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
                        <th style={{width: "16%"}}>Name</th>
                        <th style={{width: "16%"}}>Phone</th>
                        <th style={{width: "16%"}}>Address</th>
                        <th style={{width: "50%"}}>Children</th>
                    </tr>
                    </thead>
                    <tbody>
                    {relatives.map((relative) => (
                        <tr key={relative.id}>
                            <td style={{verticalAlign: "middle"}}>{relative.name}</td>
                            <td style={{verticalAlign: "middle"}}>{relative.phone}</td>
                            <td style={{verticalAlign: "middle"}}>{relative.address}</td>
                            <td style={{verticalAlign: "middle"}}>
                                {relative.childWithGroup.length > 0 ? (
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
                                            <th style={{width: "33%"}}>Name</th>
                                            <th style={{width: "33%"}}>Birth year</th>
                                            <th style={{width: "33%"}}>Group name</th>
                                            <th style={{width: "33%"}}>Teacher`s email</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {relative.childWithGroup.map((child) => (
                                            <tr key={child.id}>
                                                <td style={{verticalAlign: "middle"}}>{child.name}</td>
                                                <td style={{verticalAlign: "middle"}}>{child.birthYear}</td>
                                                <td style={{verticalAlign: "middle"}}>{child.groupName}</td>
                                                <td style={{verticalAlign: "middle"}}>{child.teacherEmail}</td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <p>No children found.</p>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <h3 style={{textAlign: "center"}}>No relatives found</h3>
            )}
        </div>
    );
}

export default AllRelatives;
