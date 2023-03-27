import {useEffect, useState} from "react";
import {Button, Form} from "react-bootstrap";
import Input from "../UI/Input/Input";
import GroupService from "../API/GroupService";

const GroupForm = ({group, setGroup, tokens, setTokens, setShowForm}) => {
    const [name, setName] = useState('');
    const [maxSize, setMaxSize] = useState(0);
    useEffect(() => {
        if (group) {
            setName(group.name);
            setMaxSize(group.maxSize);
        }
    }, [group]);
    const add = (e) => {
        e.preventDefault();
        GroupService.add(name, maxSize, tokens, setTokens).then(data => {
            if (data.ok) {
                setGroup({name: name, maxSize: maxSize, currentSize: group ? group.currentSize : 0});
                setShowForm(false);
            }
        })
    }
    const update = (e) => {
        e.preventDefault();
        GroupService.update(name, maxSize, tokens, setTokens).then(data => {
            if (data.ok) {
                setGroup({name: name, maxSize: maxSize, currentSize: group ? group.currentSize : 0});
                setShowForm(false);
            }
        })
    }

    return (
        <Form className="form" onSubmit={group ? update : add}>
            <Input type="text" placeholder="name" value={name} onChange={e => setName(e.target.value)}/>
            <Input type="number" placeholder="max size" value={maxSize} onChange={e => setMaxSize(e.target.value)}/>
            <Button type="submit"> {group ? "update" : "add"} </Button>
        </Form>
    )
}
export default GroupForm;