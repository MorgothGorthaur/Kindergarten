import {useEffect, useState} from 'react';
import AllKidsService from '../API/AllKidsService';
import Loader from '../UI/Loader/Loader';
import {Dropdown} from "react-bootstrap";

function AllChild({tokens, setTokens}) {
    const [kids, setKids] = useState([]);
    const [loader, setLoader] = useState(true);
    const [sortBy, setSortBy] = useState('default');

    const handleSortChange = (event) => {
        setSortBy(event);
    };

    useEffect(() => {
        const fetchData = async () => {
            let data;
            switch (sortBy) {
                case 'name':
                    data = await AllKidsService.getByName(tokens, setTokens);
                    break;
                case 'birth':
                    data = await AllKidsService.getByBirth(tokens, setTokens);
                    break;
                case 'groupName':
                    data = await AllKidsService.getByGroupName(tokens, setTokens);
                    break;
                case 'teacherEmail':
                    data = await AllKidsService.getByTeacherName(tokens, setTokens);
                    break;
                case 'default':
                default:
                    data = await AllKidsService.get(tokens, setTokens);
                    break;
            }
            setKids(data);
            setLoader(false);
        };
        fetchData();
    }, [sortBy, tokens, setTokens]);

    return (
        <div className="d-flex flex-column align-items-center">
            <h3>All kids</h3>
            <div className="mb-3">
                <Dropdown onSelect={handleSortChange}>
                    <Dropdown.Toggle variant="secondary" id="sort-by">
                        Sort by: {sortBy}
                    </Dropdown.Toggle>

                    <Dropdown.Menu>
                        <Dropdown.Item eventKey="default">Default</Dropdown.Item>
                        <Dropdown.Item eventKey="name">Name</Dropdown.Item>
                        <Dropdown.Item eventKey="birth">Birth year</Dropdown.Item>
                        <Dropdown.Item eventKey="groupName">Group name</Dropdown.Item>
                        <Dropdown.Item eventKey="teacherEmail">Teacher email</Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            </div>

            {loader ? (
                <div
                    style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        height: '50vh',
                    }}
                >
                    <Loader/>
                </div>
            ) : kids.length > 0 ? (
                <table
                    style={{
                        width: '100%',
                        margin: 'auto',
                        textAlign: 'center',
                        tableLayout: 'fixed',
                    }}
                >
                    <thead>
                    <tr>
                        <th style={{width: '20%'}}>Name</th>
                        <th style={{width: '20%'}}>Birth year</th>
                        <th style={{width: '30%'}}>Group name</th>
                        <th style={{width: '30%'}}>Teacher email</th>
                    </tr>
                    </thead>
                    <tbody>
                    {kids.map((kid) => (
                        <tr key={kid.id}>
                            <td style={{verticalAlign: 'middle'}}>{kid.name}</td>
                            <td style={{verticalAlign: 'middle'}}>{kid.birthYear}</td>
                            <td style={{verticalAlign: 'middle'}}>{kid.groupName}</td>
                            <td style={{verticalAlign: 'middle'}}>{kid.teacherEmail}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <h3 style={{textAlign: 'center'}}>No kids found</h3>
            )}
        </div>
    );
}

export default AllChild;